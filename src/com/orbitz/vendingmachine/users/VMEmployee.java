package com.orbitz.vendingmachine.users;
/**
 * @author Sapna Dasarath
 *
 * This class has the User information for Employee.
 * For now it serves no purpose it can later be used to add mechanisms
 * like authentication to refill the vending machine etc.
 */
public class VMEmployee implements VMUser{

	public VMEmployee(){

	}

	@Override
	public users getUserType() {
		return users.EMPLOYEE;
	}

}
