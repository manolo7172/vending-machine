package com.orbitz.vendingmachine.machine;
/**
 * @author Sapna Dasarath
 *
 * This class contains the product information.
 */
import java.util.LinkedHashMap;
import java.util.Map;

public class VMProductInfo {

	private String name;	//Name of soda
	private double cost;	//Cost of soda
	private int quantity; 	//Quantity present in vending machine
	//the slotVsQty map stores the information about the type of soda in each shelf
	private Map<String, Integer> slotVsQty = new LinkedHashMap<String, Integer>();

	public VMProductInfo() {

	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setCost(double cost) {
		this.cost = cost;
	}

	public double getCost() {
		return cost;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public int getQuantity() {
		return quantity;
	}

	public Map<String, Integer> getSlotVsQtyMap() {
		return slotVsQty;
	}

	public void addToSlot(String slot, int qty) {
		slotVsQty.put(slot, qty);
	}

	/**
	 * the slotVsQty map stores the information about the type of soda in each shelf
	 * this information can be used to dispense user selection from UI
	 * It has to be updated after the product is dispensed.
	 * @param slot the slot number on UI
	 * @param qty the qty dispensed
	 */
	public void decInSlot(String slot, int qty) {
		int curQty = slotVsQty.get(slot);
		int newqty = curQty - qty;
		if (newqty == 0) {
			slotVsQty.remove(slot);
		} else {
			slotVsQty.put(slot, newqty);
		}
		//Decrement product count
		quantity = quantity - 1;
	}
}
