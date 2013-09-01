package com.orbitz.vendingmachine.ui;

/**
 * @author Sapna Dasarath
 *
 * This class gets input from user to refill soda cans
 */
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.orbitz.vendingmachine.common.VMProperties;
import com.orbitz.vendingmachine.machine.VMProductInfo;
import com.orbitz.vendingmachine.main.VMController;

public class VMRefill extends JDialog {

	private static final long serialVersionUID = 1L;
	private VMController controller = null;
	private VMFrame frame = null;

	public VMRefill(VMFrame frame, VMController controller) {
		super(frame, true);
		this.controller = controller;
		this.frame = frame;
		createUI();
	}

	/**
	 * Product cost Quantity Refill Soda1 1.25 5 3 implies 5 cans of soda 1
	 * exists and costs 1.25 each refill it with 3 more cans
	 */
	private void createUI() {

		Map<String, VMProductInfo> productInfo = controller.getProductInfo();
		final Map<JTextField, String> textVsProdName = new HashMap<JTextField, String>();

		JPanel panel = new JPanel(new GridLayout(productInfo.size() + 2, 1));

		JPanel header = new JPanel(new GridLayout(1, 4));
		JLabel nameheader = new JLabel("Product");
		header.add(nameheader);

		JLabel proceheader = new JLabel("Cost");
		header.add(proceheader);

		JLabel qtyheader = new JLabel("Quantity");
		header.add(qtyheader);

		JLabel prodqtyheader = new JLabel("Refill");
		header.add(prodqtyheader);

		panel.add(header);

		for (String name : productInfo.keySet()) {
			VMProductInfo prod = productInfo.get(name);
			JPanel inpanel = new JPanel(new GridLayout(1, 4));
			JLabel nameLabel = new JLabel(name);
			inpanel.add(nameLabel);

			JLabel priceLabel = new JLabel(prod.getCost() + "");
			inpanel.add(priceLabel);

			JLabel qtyLabel = new JLabel(prod.getQuantity() + "");
			inpanel.add(qtyLabel);

			JTextField qtyText = new JTextField();
			inpanel.add(qtyText);
			textVsProdName.put(qtyText, name);

			panel.add(inpanel);
		}

		JPanel header1 = new JPanel(new GridLayout(1, 4));
		JLabel nameheader1 = new JLabel("");
		header1.add(nameheader1);

		JLabel proceheader1 = new JLabel("");
		header1.add(proceheader1);

		JLabel qtyheader1 = new JLabel();
		String text = "<html>" + "Max cans" + "<br>" + "allowed ="
				+ VMProperties.getInstance().getTotalNumberOfSodaCans()
				+ "</html>";
		qtyheader1.setText(text);
		header1.add(qtyheader1);

		final JButton donebutton = new JButton("Done");
		donebutton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				processDoneAction(textVsProdName);
			}
		});
		header1.add(donebutton);
		panel.add(header1);

		this.getContentPane().add(panel);
	}

	private void processDoneAction(Map<JTextField, String> textVsProdName) {
		boolean retval = validateAndUpdate(textVsProdName);
		if (retval) {
			updateProductInfoInUI();
			updateUI();
			closeWindow();
		}
	}

	/**
	 * check if refill is valid
	 *
	 * @param textVsProdName
	 * @return
	 */
	private boolean validateAndUpdate(Map<JTextField, String> textVsProdName) {
		System.out.println("Checking if product vcan be added.");
		Map<String, VMProductInfo> productInfo = controller.getProductInfo();
		int max = VMProperties.getInstance().getTotalNumberOfSodaCans();
		int perline = VMProperties.getInstance().getNumberOfSodaPerLineInACol();

		int currentcount = 0;
		int newcount = 0;
		int boxcount = 0;

		Map<String, Integer> totalcount = new LinkedHashMap<String, Integer>();
		for (JTextField field : textVsProdName.keySet()) {

			VMProductInfo prod = productInfo.get(textVsProdName.get(field));
			currentcount = currentcount + prod.getQuantity();

			String val = field.getText();
			if (!val.equals(""))
				try {
					Integer intval = Integer.parseInt(val);
					newcount = newcount + intval;

					boxcount = prod.getQuantity() + intval;
					totalcount.put(prod.getName(), boxcount);
				} catch (Exception e) {
					frame.showErrorDialog("Invalid input, Allowed values are Integers only");
					return false;
				}
		}

		// If there has been no update return.
		if (currentcount == newcount) {
			closeWindow();
			return false;
		}

		// If the current count exceeds max size return
		if ((currentcount + newcount) > max) {
			frame.showErrorDialog("Total Number of cans exceed max can count.");
			return false;
		}

		int totalboxcount = 0;
		for (String name : totalcount.keySet()) {
			int count = totalcount.get(name);
			if (count > 0) {
				int mod = count % perline;
				mod = perline - mod;
				totalboxcount = totalboxcount + (count + mod);
			}
		}

		// Since only a given type of product can be stocked in a shelf
		// If all shelves are taken return
		if (totalboxcount > max) {
			frame.showErrorDialog("<html>"
					+ "Total Number of cans per shelf exceed max cans that can be put in a line."
					+ "<br>"
					+ "Ex: soda 1 - 96 and soda 2 - 4 is an invalid combination."
					+ "<br>"
					+ " Change it to soda 1 - 90 or 95 and soda 2- 4 or 0."
					+ "</html>");
			return false;
		}

		// Else update product info
		controller.updateProductInfo(totalcount);
		return true;
	}

	/**
	 * update product info list in machine
	 */
	private void updateProductInfoInUI() {
		// int rows = VMProperties.getInstance().getNumberOfShelves();
		int cols = VMProperties.getInstance().getNumberOfColumnsPerShelf();
		int perline = VMProperties.getInstance().getNumberOfSodaPerLineInACol();

		char row = 'A';
		int col = 1;

		for (String name : controller.getProductInfo().keySet()) {
			VMProductInfo info = controller.getProductInfo().get(name);
			int qty = info.getQuantity();

			// Get the remainder to see how many full slots can this product
			// take
			// Get the mod to see how many partial slots does this product takes
			int mod = qty % perline;
			int rem = qty / perline;

			if (rem != 0) {
				for (int i = 0; i < rem; i++) {
					String selection = "" + row + col;
					info.addToSlot(selection, perline);
					col++;
					if (col > cols) {
						row++;
						col = 1;
					}
				}
			}
			if (mod != 0) {
				String selection = "" + row + col;
				info.addToSlot(selection, mod);
				col++;
				if (col > cols) {
					row++;
					col = 1;
				}
			}
		}
	}

	/**
	 * update UI
	 */
	private void updateUI() {
		controller.updateUIpostRefill();
	}

	/**
	 * method called to close the UI
	 */
	private void closeWindow() {
		this.dispose();
	}

	/**
	 * method to render UI
	 */
	public void showFrame() {
		this.pack();
		Dimension size = new Dimension(400, 300);
		this.setSize(size);
		this.setMaximumSize(size);
		this.setResizable(false);
		this.setTitle("Refill Vending Machine");
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setVisible(true);
	}
}
