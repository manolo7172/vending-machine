package com.orbitz.vendingmachine.ui;
/**
 * @author Sapna Dasarath
 *
 * This class shows the usage of the program
 */
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.orbitz.vendingmachine.main.VMController;

public class VMHelp extends JDialog {

	private static final long serialVersionUID = 1L;
	private VMController controller = null;
	private VMFrame frame = null;

	public VMHelp(VMFrame frame, VMController controller) {
		super(frame, true);
		this.controller = controller;
		this.frame = frame;
		createUI();
	}

	private void createUI() {
		JPanel panel = new JPanel(new GridLayout(4, 1));

		JLabel insertcoins = new JLabel();
		String inserttext = "<html>" + "To add money:" + "<br>"
				+ "Click on Insert Coins" + "<br>" + "Add required currency"
				+ "<br>" + "Click on done" + "<br>"
				+ "value gets updated on UI" + "<br>" + "</html>";
		insertcoins.setText(inserttext);
		panel.add(insertcoins);

		JLabel removecoins = new JLabel();
		String removetext = "<html>" + "To get change:" + "<br>"
				+ "Click on remove Coins" + "<br>"
				+ "Collect coins in Dispenser" + "<br>" + "</html>";
		removecoins.setText(removetext);
		panel.add(removecoins);

		JLabel getproduct = new JLabel();
		String getprodtext = "<html>" + "To buy product:" + "<br>"
				+ "Insert Coins" + "<br>" + "Select product and click on done."
				+ "<br>" + "To change selection click on clear" + "<br>"
				+ "To get the remaining money select return coins" + "<br>"
				+ "</html>";
		getproduct.setText(getprodtext);
		panel.add(getproduct);

		JLabel refill = new JLabel();
		String refilltext = "<html>" + "To refill machine:" + "<br>"
				+ "Click on refill" + "<br>" + "Add required soda cans"
				+ "<br>" + "Click on done" + "<br>" + "</html>";
		refill.setText(refilltext);
		panel.add(refill);

		this.getContentPane().add(panel);
	}

	/**
	 * method to render UI
	 */
	public void showFrame() {
		this.pack();
		Dimension size = new Dimension(500, 500);
		this.setSize(size);
		this.setMaximumSize(size);
		this.setResizable(false);
		this.setTitle("Help for Vending Machine");
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setVisible(true);
	}
}
