package com.orbitz.vendingmachine.ui;

/**
 * @author Sapna Dasarath
 *
 * This is the main class that renders the UI and shows user the current balance and soda counts
 */
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import com.orbitz.vendingmachine.common.VMProperties;
import com.orbitz.vendingmachine.machine.VMProductInfo;
import com.orbitz.vendingmachine.main.VMController;

public class VMFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private VMController controller = null;
	private StringBuffer input = new StringBuffer(50);

	private JPanel displayBannerPanel = null;
	private JPanel displayBoxPanel = null;
	private JPanel transactionPanel = null;
	private JPanel dispenserPanel = null;

	private JLabel selectionOutLabel = null;
	private JLabel changeLeftOutLabel = null;
	private JLabel dispenserOutLabel = null;

	public VMFrame(VMController controller) {
		this.controller = controller;
		createUI();
	}

	/**
	 * Create UI, add all the panels to JFrame
	 */
	private void createUI() {
		displayBannerPanel = createDisplayBanner();
		displayBoxPanel = createDisplayBox();
		transactionPanel = createTransactionBox();
		dispenserPanel = createDispenserBox();
		addPanels();
	}

	private void addPanels() {
		JPanel panel = new JPanel(new BorderLayout());
		// Add panels
		panel.add(displayBannerPanel, BorderLayout.NORTH);
		displayBannerPanel.setMaximumSize(new Dimension(500, 100));

		panel.add(displayBoxPanel, BorderLayout.CENTER);
		displayBoxPanel.setMaximumSize(new Dimension(400, 500));

		panel.add(transactionPanel, BorderLayout.EAST);
		transactionPanel.setMaximumSize(new Dimension(200, 500));

		panel.add(dispenserPanel, BorderLayout.SOUTH);
		dispenserPanel.setMaximumSize(new Dimension(500, 100));

		Dimension size = new Dimension(600, 600);

		this.getContentPane().removeAll();
		this.getContentPane().add(panel);
		this.getContentPane().repaint();
		this.pack();
		this.setSize(size);
		this.setMaximumSize(size);
		this.repaint();
	}

	private JPanel createDisplayBanner() {
		// Add a label and put it in a panel and return.
		JPanel panel = new JPanel();
		panel.setBorder(LineBorder.createBlackLineBorder());

		JLabel label = new JLabel();
		label.setText(VMProperties.getInstance().getSodaLabel());
		panel.add(label);
		return panel;
	}

	private JPanel createDisplayBox() {
		// create a grid and color each grid based on product available.
		int rows = VMProperties.getInstance().getNumberOfShelves();
		int cols = VMProperties.getInstance().getNumberOfColumnsPerShelf();

		JPanel panel = new JPanel(new GridLayout(rows, cols));

		char val = 'A';
		String currencySupported = VMProperties.getInstance()
				.getCurrencySupported();
		for (int i = 0; i < rows; i++) {
			JPanel innerpanel = new JPanel(new GridLayout(1, 1));
			innerpanel.setBorder(LineBorder.createGrayLineBorder());
			for (int j = 0; j < cols; j++) {

				JLabel label = new JLabel();
				String position = "" + val + (j + 1);
				String count = "0";
				String name = "Empty";
				String cost = "0.0";

				VMProductInfo info = controller.getInfoForSeletion(position);
				if (info != null) {
					count = String
							.valueOf(info.getSlotVsQtyMap().get(position));
					name = info.getName();
					cost = String.valueOf(info.getCost());
				}

				String text = "<html>" + position + "-Qty=" + count + "<br>"
						+ name + "-" + currencySupported + cost + "</html>";
				label.setText(text);
				innerpanel.add(label);
			}
			val++;
			panel.add(innerpanel);
		}

		return panel;
	}

	private JPanel createTransactionBox() {
		// Create all tthe boxes that take user input
		int rowsInBox = VMProperties.getInstance().getNumberOfShelves();
		int colsInBox = VMProperties.getInstance().getNumberOfColumnsPerShelf();

		int rows = Math.max(rowsInBox, colsInBox);
		int cols = 2;

		JPanel panel = new JPanel(new GridLayout(rows + 8, 1));
		panel.setBorder(LineBorder.createGrayLineBorder());
		panel.setMaximumSize(new Dimension(200, 400));

		JLabel changeLeft = new JLabel();
		changeLeftOutLabel = changeLeft;
		changeLeft.setText("Insert coins..");
		panel.add(changeLeft);

		JButton insertCoins = new JButton();
		insertCoins.setText("Insert Coins");
		insertCoins.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				insertCoinPressed();
			}
		});
		panel.add(insertCoins);

		JButton returnCoins = new JButton();
		returnCoins.setText("Return Coins");
		returnCoins.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				returnChangePressed();
			}
		});
		panel.add(returnCoins);

		JLabel selectionLabel = new JLabel();
		selectionOutLabel = selectionLabel;
		selectionLabel.setText("Selection..");
		panel.add(selectionLabel);

		JButton doneButton = new JButton();
		doneButton.setText("Done");
		doneButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispenseSoda();
			}
		});
		panel.add(doneButton);

		JPanel clearPanel = new JPanel(new GridLayout(1, 1));
		JButton clearButton = new JButton();
		clearButton.setText("Clear");
		clearButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				clearSodaSelected();
			}
		});
		clearPanel.add(clearButton);
		panel.add(clearPanel);

		char val = 'A';
		for (int i = 0; i < rows; i++) {
			JPanel innerpanel = new JPanel(new GridLayout(1, 2));

			for (int j = 0; j < cols; j++) {

				if (j == 0) {
					if (i < rowsInBox) {
						final JButton button = new JButton();
						button.setText("" + val);
						button.addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent e) {
								appendSelection(button.getText());
							}
						});
						innerpanel.add(button);
					} else {
						innerpanel.add(new JPanel());
					}
				} else if (j == 1) {
					if (i < colsInBox) {
						final JButton button = new JButton();
						button.setText("" + (i + 1));
						button.addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent e) {
								appendSelection(button.getText());
							}
						});
						innerpanel.add(button);
					} else {
						innerpanel.add(new JPanel());
					}
				}
			}
			val++;
			panel.add(innerpanel);
		}

		JButton button = new JButton();
		button.setText("Refill");
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				refillMachinePressed();
			}
		});
		panel.add(button);

		JButton helpbutton = new JButton();
		helpbutton.setText("Help");
		helpbutton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				showHelp();
			}
		});
		panel.add(helpbutton);

		return panel;
	}

	private JPanel createDispenserBox() {
		// This box shows the output of transactions
		JPanel panel = new JPanel();
		panel.setBorder(LineBorder.createBlackLineBorder());
		JLabel label = new JLabel();
		dispenserOutLabel = label;
		label.setText("Dispenser Output");
		panel.add(label);
		return panel;
	}

	/**
	 * handle insert coin event
	 */
	private void insertCoinPressed() {
		controller.insertCoinPressed();
		VMInsertCoins insertcoins = new VMInsertCoins(this, controller);
		insertcoins.showFrame();
	}

	/**
	 * handle get change event
	 */
	private void returnChangePressed() {
		controller.getChangePressed();
	}

	/**
	 * handle product selection event
	 */
	private void appendSelection(CharSequence inputstr) {
		input.append(inputstr);
		selectionOutLabel.setText(input.toString());
	}

	/**
	 * handle get product event check if product is available and sufficient
	 * balance exists
	 */
	private void dispenseSoda() {
		if (controller.getExistingCash() == 0) {
			showErrorDialog("Insufficient money");
			return;
		}

		Pattern pattern = Pattern.compile("[A-Z]{1}[1-9]{1}");
		Matcher matcher = pattern.matcher(input.toString());
		if (!matcher.matches()) {
			showErrorDialog("Invalid Input Selection");
			return;
		}

		controller.dispenseSoda(input.toString());
		clearSodaSelected();
	}

	/**
	 * handle clear soda selection event
	 */
	private void clearSodaSelected() {
		input = new StringBuffer("");
		selectionOutLabel.setText(input.toString());
	}

	/**
	 * handle refill machine event
	 */
	private void refillMachinePressed() {
		VMRefill refill = new VMRefill(this, controller);
		refill.showFrame();
	}

	/**
	 * SHow help for actions
	 */
	private void showHelp() {
		VMHelp help = new VMHelp(this, controller);
		help.showFrame();
	}

	/**
	 * Update status of soda cans
	 */
	public void updateDisplayBoxUI() {
		displayBoxPanel = null;
		displayBoxPanel = createDisplayBox();
		addPanels();
	}

	/**
	 * update status of dispenser to show change/ product dispensed
	 *
	 * @param status
	 */
	public void updateDispenserStatus(String status) {
		dispenserOutLabel.setText(status);
		dispenserOutLabel.repaint();
	}

	/**
	 * update status of cash in machine
	 *
	 * @param totalCoins
	 */
	public void updateChangeStatus(double totalCoins) {
		changeLeftOutLabel.setText(String.valueOf(totalCoins));
		changeLeftOutLabel.repaint();
	}

	/**
	 * utility method to show errors
	 *
	 * @param string
	 */
	public void showErrorDialog(String string) {
		updateDispenserStatus(string);
		JOptionPane.showMessageDialog(null, string);
		return;
	}

	/**
	 * method to render UI
	 */
	public void showFrame() {
		Dimension size = new Dimension(600, 600);
		this.setSize(size);
		this.setMaximumSize(size);
		this.setResizable(false);
		this.setTitle("Vending Machine");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
	}

}
