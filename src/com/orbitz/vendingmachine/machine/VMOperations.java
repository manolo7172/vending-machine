package com.orbitz.vendingmachine.machine;

/**
 * @author Sapna Dasarath
 *
 *         This class defines the interface for the operations valid on the
 *         Vending Machine.
 */
public interface VMOperations {

	// Add cash from user
	public void insertCoin(double totalCoins);

	// Return change
	public double returnChange();

	// Dispense soda can
	public void dispenseCan(String selection, String sodaname, int qty);

	// Refill soda cans
	public void refillCans(int cansAdded);
}
