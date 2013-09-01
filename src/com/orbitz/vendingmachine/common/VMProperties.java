package com.orbitz.vendingmachine.common;

/**
 * @author Sapna Dasarath
 *
 * This class is used to load the properties file and set the default values
 * for the program.
 * We set the following properties here
 * 1. Vending Machine Name.
 * 2. No Of Shelves in the Vending machine.
 * 3. No of columns in each Shelf.
 * 4. No of Soda cans that can be placed in each column.
 * 5. Currency type supported.
 * 6. Input Currency denominations supported.
 * 7. Output Currency denominations supported.
 * 8. Soda Types and it's cost.
 *
 * It also supports utility methods that use the above parameters.
 */
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class VMProperties {

	private static VMProperties machineProp = null;
	Set<Double> inputDenomVals = new LinkedHashSet<Double>();
	Set<Double> outputDenomVals = new LinkedHashSet<Double>();
	Map<String, Double> sodaNames = new LinkedHashMap<String, Double>();

	private VMProperties() {
		readPropertiesFile();
	}

	public static VMProperties getInstance() {
		if (machineProp == null) {
			machineProp = new VMProperties();
		}
		return machineProp;
	}

	private void readPropertiesFile() {
		// TODO convert this method to read properties from a file

	}

	/**
	 *
	 * @return Vending Machine Name.
	 */
	public String getSodaLabel() {
		return "Super Soda Dispenser";
	}

	/**
	 *
	 * @return No Of Shelves in the Vending machine.
	 */
	public int getNumberOfShelves() {
		return 5;
	}

	/**
	 *
	 * @return No of columns in each Shelf.
	 */
	public int getNumberOfColumnsPerShelf() {
		return 4;
	}

	/**
	 *
	 * @return No of Soda cans that can be placed in each column.
	 */
	public int getNumberOfSodaPerLineInACol() {
		return 5;
	}

	/**
	 *
	 * @return Currency type supported.
	 */
	public String getCurrencySupported() {
		return "$";
	}

	/**
	 *
	 * @return Input Currency denominations supported.
	 */
	public Set<Double> getInputDenomSupported() {
		inputDenomVals.add(0.05);
		inputDenomVals.add(0.10);
		inputDenomVals.add(0.25);
		inputDenomVals.add(0.25);
		inputDenomVals.add(1.00);
		inputDenomVals.add(5.00);
		inputDenomVals.add(10.00);
		return inputDenomVals;
	}

	/**
	 *
	 * @return Output Currency denominations supported.
	 */
	public Set<Double> getOutputDenomSupported() {
		outputDenomVals.add(0.05);
		outputDenomVals.add(0.10);
		outputDenomVals.add(0.25);
		return outputDenomVals;
	}

	/**
	 *
	 * @return Soda Types and it's cost.
	 */
	public Map<String, Double> getSodaTypes() {
		sodaNames.put("Soda 1", 1.25);
		sodaNames.put("Soda 2", 1.00);
		sodaNames.put("Soda 3", 2.25);
		sodaNames.put("Soda 4", 1.25);
		sodaNames.put("Soda 5", 1.25);
		return sodaNames;
	}

	/**
	 *
	 * @return total capacity of the vending machine
	 */
	public int getTotalNumberOfSodaCans() {
		return getNumberOfShelves() * getNumberOfColumnsPerShelf()
				* getNumberOfSodaPerLineInACol();
	}

	/**
	 *
	 * @param selection
	 *            name of the soda selected
	 * @param qty
	 *            quantity selected
	 * @return total cost of the selection
	 */
	public double getCostofProduct(String selection, int qty) {
		double totalcost = 0;
		Map<String, Double> sodaNames = getSodaTypes();
		if (sodaNames.containsKey(selection)) {
			double cost = sodaNames.get(selection);
			totalcost = cost * qty;
		}
		return totalcost;
	}
}
