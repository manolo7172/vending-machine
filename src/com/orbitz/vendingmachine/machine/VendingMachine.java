package com.orbitz.vendingmachine.machine;

/**
 * @author Sapna Dasarath
 *
 * This class implements the vending machine interface.
 * It has the operations supported by user and employee.
 */
import java.util.LinkedHashMap;
import java.util.Map;

import com.orbitz.vendingmachine.common.VMProperties;
import com.orbitz.vendingmachine.main.VMController;
import com.orbitz.vendingmachine.users.VMBuyer;
import com.orbitz.vendingmachine.users.VMEmployee;
import com.orbitz.vendingmachine.users.VMUser;

public class VendingMachine implements VMOperations {

	private static VendingMachine machine = null;
	private VMController controller = null;
	private VMUser user = null;
	private Map<String, VMProductInfo> productInfo = new LinkedHashMap<String, VMProductInfo>();

	private VendingMachine(VMController controller) {
		this.controller = controller;
		createProductInfo();
	}

	/**
	 *
	 * @param controller
	 * @return singleton instance of vending machine
	 */
	public static VendingMachine getInstance(VMController controller) {
		if (machine == null) {
			machine = new VendingMachine(controller);
		}
		return machine;
	}

	/**
	 * Keep a list of products that can be added to this machine
	 */
	private void createProductInfo() {
		System.out.println("Creating products for Vending machine");
		Map<String, Double> products = VMProperties.getInstance()
				.getSodaTypes();
		for (String name : products.keySet()) {
			VMProductInfo prod = new VMProductInfo();
			prod.setName(name);
			prod.setCost(products.get(name));
			prod.setQuantity(0);
			productInfo.put(name, prod);
		}
	}

	/**
	 *
	 * @return list of products that can be added to this machine
	 */
	public Map<String, VMProductInfo> getProductInfo() {
		return productInfo;
	}

	/**
	 *
	 * @param type
	 *            the type of user currently operating the machine.
	 */
	public void createUser(VMUser.users type) {
		if (user == null) {
			if (type == VMUser.users.BUYER) {
				user = new VMBuyer();
			} else if (type == VMUser.users.EMPLOYEE) {
				user = new VMEmployee();
			}
		}
	}

	/**
	 * remove user
	 */
	public void removeUser() {
		user = null;
	}

	/**
	 * Add cash from user to the machine
	 */
	@Override
	public void insertCoin(double totalCoins) {
		if (user != null) {
			System.out.println("Adding cash to user account");
			((VMBuyer) user).addCash(totalCoins);
		}
	}

	/**
	 * return the change from user transaction
	 */
	@Override
	public double returnChange() {
		double change = 0;
		if (user != null) {
			System.out.println("Getting change from user account");
			change = ((VMBuyer) user).getChange();
		}
		return change;
	}

	/**
	 * Checks if the product is available and user has sufficient balance
	 *
	 * @param sodaName
	 *            name of product
	 * @param qty
	 *            quantity, for now this is always one
	 * @return if the transaction can be processed.
	 */
	public boolean canDispenseCan(String sodaName, int qty) {
		System.out.println("Checking balance and product availability");
		double cost = VMProperties.getInstance()
				.getCostofProduct(sodaName, qty);
		if (user != null) {
			if (((VMBuyer) user).canDispenseProduct(cost)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * for a given soda type , dispense the product
	 */
	@Override
	public void dispenseCan(String selection, String sodaname, int qty) {
		System.out.println("Dispensing soda can");
		if (user != null) {
			double cost = VMProperties.getInstance().getCostofProduct(sodaname,
					qty);
			VMProductInfo info = controller.getProductInfo().get(sodaname);
			//Dispense the product and update the required product info.
			((VMBuyer) user).dispenseProduct(cost);
			info.decInSlot(selection, qty);
		}
	}

	@Override
	public void refillCans(int cansAdded) {

	}
}
