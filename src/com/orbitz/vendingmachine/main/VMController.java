package com.orbitz.vendingmachine.main;

/**
 * @author Sapna Dasarath
 *
 * This class is the interface between the UI and the vending machine class.
 */
import java.util.Map;
import java.util.StringTokenizer;

import javax.swing.SwingUtilities;

import com.orbitz.vendingmachine.machine.VMProductInfo;
import com.orbitz.vendingmachine.machine.VendingMachine;
import com.orbitz.vendingmachine.ui.VMFrame;
import com.orbitz.vendingmachine.users.VMUser;

public class VMController {
	private VMFrame frame = null;
	private VendingMachine machine = null;

	public VMController() {
		System.out.println("Creating the vending machine and show UI");
		// Create and render the UI in EDT
		createUI();
		// Create the vending machine
		createMachine();
	}

	/**
	 * create the UI on EDT
	 */
	private void createUI() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				frame = new VMFrame(VMController.this);
				frame.showFrame();
			}
		});
	}

	/**
	 * Create a singleton interface for the machine
	 */
	private void createMachine() {
		machine = VendingMachine.getInstance(this);
	}

	/**
	 * Access method to product info list in Vending machine.
	 *
	 * @return
	 */
	public Map<String, VMProductInfo> getProductInfo() {
		return machine.getProductInfo();
	}

	/**
	 * Access method to user cash in Vending machine.
	 *
	 * @return
	 */
	public double getExistingCash() {
		return machine.returnChange();
	}

	/**
	 * Action called when refill is pressed on UI Ask user to enter the values
	 * for Soda and redraw the UI
	 */
	public void insertRefillPressed() {
		machine.createUser(VMUser.users.EMPLOYEE);
	}

	/**
	 * Action to be performed post Refill action Redraw the UI to reflect new
	 * counts
	 */
	public void updateUIpostRefill() {
		frame.updateDisplayBoxUI();
	}

	/**
	 * Action called when user wants to insert coins Display allowed options and
	 * update current money balance
	 */
	public void insertCoinPressed() {
		machine.createUser(VMUser.users.BUYER);
	}

	/**
	 * Action used to update balance in vending machine
	 *
	 * @param totalCoins
	 */
	public void insertMoney(double totalCoins) {
		machine.insertCoin(totalCoins);
		frame.updateChangeStatus(machine.returnChange());
	}

	/**
	 * The change has to be returned in minimal count of $0.25,$0.10 and $0.05
	 * We get the change from machine and compute the values here.
	 */
	public void getChangePressed() {
		double change = machine.returnChange();
		if (change > 0) {

			int quarters = 0;
			int dime = 0;
			int nickel = 0;

			// Break the existing change into integer and deimal values
			// The integer values can be broken into quarters
			// For the decimal values use quarters, dimes and nickel.
			StringTokenizer tokens = new StringTokenizer(
					String.valueOf(change), ".");

			if (tokens.countTokens() == 2) {
				// The Integer value of existing change
				String intval = tokens.nextToken();
				quarters = Integer.parseInt(intval) * 4;

				// The decimal value of existing change
				String decval = tokens.nextToken();
				int decint = Integer.parseInt(decval);

				int rem = decint;
				int mod;

				while (rem != 0) {
					mod = rem % 25;
					if (mod == 0) {

						mod = rem % 10;
						if (mod == 0) {
							mod = rem % 5;

							if (mod == 0) {
								// Do nothing
							} else {
								nickel = nickel + mod;
								rem = rem / 5;
							}
						} else {
							dime = dime + mod;
							rem = rem / 10;
						}

					} else {
						quarters = quarters + mod;
						rem = rem / 25;
					}
				}

			}

			// Update the UI
			StringBuffer sb = new StringBuffer("Please collect your change: ");
			sb.append("$0.25=").append(quarters).append(", $0.10=")
					.append(dime).append(", $0.05=").append(nickel);
			frame.updateDispenserStatus(sb.toString());

			// Remove this user session
			machine.removeUser();
		} else {
			frame.updateDispenserStatus("No Change to return.");
		}
		frame.updateChangeStatus(machine.returnChange());
	}

	public void dispenseSoda(String selection) {
		VMProductInfo info = getInfoForSeletion(selection);
		if (info == null) {
			frame.showErrorDialog("Product Not Available");
			return;
		}

		boolean canDispenseCan = machine.canDispenseCan(info.getName(), 1);
		if (!canDispenseCan) {
			frame.showErrorDialog("Insufficient funds");
			return;
		}
		machine.dispenseCan(selection, info.getName(), 1);
		frame.updateDisplayBoxUI();
		frame.updateChangeStatus(machine.returnChange());
		frame.updateDispenserStatus("Please pick up your soda.");
	}

	/**
	 * This method is used to map user selection to product name Ex: A1 maps to
	 * soda1
	 *
	 * @param selection
	 *            user selection
	 * @return product type corresponding to soda name
	 */
	public VMProductInfo getInfoForSeletion(String selection) {
		Map<String, VMProductInfo> prodinfo = getProductInfo();
		for (String name : prodinfo.keySet()) {
			VMProductInfo tempinfo = prodinfo.get(name);
			if (tempinfo.getSlotVsQtyMap().containsKey(selection)) {
				return tempinfo;
			}
		}
		return null;
	}

	/**
	 * This method is used to update product info after the soda is dispensed
	 * @param totalboxcount
	 */
	public void updateProductInfo(Map<String, Integer> totalboxcount) {
		for (String name : totalboxcount.keySet()) {
			VMProductInfo prod = getProductInfo().get(name);
			int newval = prod.getQuantity() + totalboxcount.get(name);
			prod.setQuantity(newval);
		}
	}
}
