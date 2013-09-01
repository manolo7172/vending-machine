package com.orbitz.vendingmachine.users;
/**
 * @author Sapna Dasarath
 *
 * This class defines the user interface.
 */
public interface VMUser {

	public enum users{
	    BUYER, EMPLOYEE;
	};

	public users getUserType();
}
