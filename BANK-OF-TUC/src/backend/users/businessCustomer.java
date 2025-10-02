package backend.users;

import java.util.Stack;

import backend.accounts.Account;
import backend.accounts.Branch;
import backend.accounts.BusinessAccount;
import backend.accounts.Transaction;

public class businessCustomer extends User{
	private Branch branch;
	public businessCustomer(String userID, String password, String email, String businessName, String representativeName, String phoneNumber) {
		super(userID, password, email, businessName, representativeName, phoneNumber);
	}
	//na doume ti prepei na einai protected
	
	
	
	protected void createAccount() {
    	Account newBusinessAccount = new BusinessAccount(this.userID, 0.0, new Stack<Transaction>(), branch);    	
    	//κανω initialize αλλα δεν το αποθηκευω πουθενα
	}
	
	protected void askToCloseAccount() {
		//
	}
	

	protected void viewAccountBalance() {
		
	}
	
	protected void viewAccountDetails() {
		
	}
	
	protected void transferMoney() {
		
	}
	
	
	protected void payBills() {
		
	}
	
	
	protected void viewTransactionHistory() {
		
	}
	
	protected void updatePersonalInformation() {
		
	}

}
