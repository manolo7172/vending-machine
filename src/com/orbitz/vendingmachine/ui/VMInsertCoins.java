package com.orbitz.vendingmachine.ui;

/**
 * @author Sapna Dasarath
 *
 * This class get the input from user to update cash
 */
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.orbitz.vendingmachine.common.VMProperties;
import com.orbitz.vendingmachine.main.VMController;

public class VMInsertCoins extends JDialog {
	private static final long serialVersionUID = 1L;
	private VMController controller = null;
	private VMFrame frame = null;

	public VMInsertCoins(VMFrame frame, VMController controller) {
		super(frame, true);
		this.controller = controller;
		this.frame = frame;
		createUI();
	}

	private void createUI() {
		Set<Double> inputdenom = VMProperties.getInstance()
				.getInputDenomSupported();

		final Map<JTextField, Double> textBoxSet = new LinkedHashMap<JTextField, Double>();

		JPanel panel = new JPanel(new GridLayout(inputdenom.size() + 2, 1));

		JPanel header = new JPanel(new GridLayout(1, 2));
		JLabel nameheader = new JLabel("Currency");
		header.add(nameheader);

		JLabel proceheader = new JLabel("Qty");
		header.add(proceheader);

		panel.add(header);

		for (Double input : inputdenom) {
			JPanel inpanel = new JPanel(new GridLayout(1, 2));

			JLabel priceLabel = new JLabel(String.valueOf(input));
			inpanel.add(priceLabel);

			JTextField qtyText = new JTextField();
			inpanel.add(qtyText);
			textBoxSet.put(qtyText, input);
			panel.add(inpanel);
		}

		JPanel header1 = new JPanel(new GridLayout(1, 2));
		JLabel nameheader1 = new JLabel("");
		String text = "<html>" + "Current Cash=" + "<br>"
				+ controller.getExistingCash() + "</html>";
		nameheader1.setText(text);

		header1.add(nameheader1);

		final JButton donebutton = new JButton("Done");
		donebutton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				insertMoney(textBoxSet);
				closeWindow();
			}
		});
		header1.add(donebutton);
		panel.add(header1);

		this.getContentPane().add(panel);
	}

	/**
	 * get money added in UI and add it to existing currency
	 *
	 * @param textBoxSet
	 */
	private void insertMoney(Map<JTextField, Double> textBoxSet) {
		double count = 0;
		for (JTextField field : textBoxSet.keySet()) {
			String val = field.getText();
			if (!val.equals("")) {
				try {
					int countval = Integer.parseInt(val);
					Double type = textBoxSet.get(field);
					count = count + (type * countval);
				} catch (Exception e) {
					frame.showErrorDialog("Invalid input, Allowed values are Integers only");
					return;
				}
			}
		}
		controller.insertMoney(count);
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
		Dimension size = new Dimension(300, 400);
		this.setSize(size);
		this.setMaximumSize(size);
		this.setResizable(false);
		this.setTitle("Insert Coins");
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setVisible(true);
	}
}
