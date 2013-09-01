package com.orbitz.vendingmachine.users;
/**
 * @author Sapna Dasarath
 *
 * This class has the User information for Buyer
 */
public class VMBuyer implements VMUser {
	private double money = 0;

	public VMBuyer() {

	}

	@Override
	public users getUserType() {
		return users.BUYER;
	}

	/**
	 * Add cash to user account
	 * @param cashAdded
	 */
	public void addCash(double cashAdded) {
		money = money + cashAdded;
	}

	/**
	 *
	 * @return get current change
	 */
	public double getChange() {
		return money;
	}

	/**
	 *
	 * @param totalcost
	 * @return true if the balance is suffcient to dispense product
	 */
	public boolean canDispenseProduct(double totalcost) {
		return money >= totalcost;
	}

	/**
	 * dispense product and remove cash from user account
	 * @param cashRemove
	 */
	public void dispenseProduct(double cashRemove) {
		money = money - cashRemove;
	}
}
