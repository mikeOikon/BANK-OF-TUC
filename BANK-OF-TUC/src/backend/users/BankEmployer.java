package backend.users;

import backend.Branch;
import backend.accounts.Account;
import behaviors.EmployeeBehavior;
import types.UserType;

public class BankEmployer extends User {

	public BankEmployer(String userID,String username, String password, String name, String surname, Branch branch, String AFM) {
		super(userID, username,password,name,surname, branch,AFM);
		this.userBehavior= new EmployeeBehavior();
	}
	
	public UserType getUserType() {
		return UserType.EMPLOYEE;
	}

	//na doume ti prepei na einai protected
	
	protected void manageCustomerAccounts() {
		//πχ να κανει accept ή reject account
	}
	
	protected void assistCustomers() {
		
	}
	
	protected void acceptTransactions() {
		
	}
	
	protected void rejectTransactions() {
		
	}
	
	protected void acceptAccount() {
		
	}
	
	protected void rejectAccount() {
	
	}
	
	protected void monitorAccountActivities() {
		//πχ να βλεπει transactions
	}
	
	protected void generateReports() {
		//πχ για τους admin
	}
	
	protected void updateCustomerInformation() {
		//πχ να διορθωνει λαθη στα στοιχεια
	}
	
	protected void updateCustomerAccountDetails() {
		//πχ να κανει reset password, να κλεινει account (οχι να τα διαγραφει), να κανει update limit συναλλαγων
	}
	
	public String getFullName() {
	    return this.name + " " + this.surname;
	}
	
	public boolean deleteUserAccount(User user, String IBAN) {
	    if (user == null) {
	        return false;
	    }

	    if (user instanceof Customer) {
	    	Customer customer = (Customer) user;
	        Account accToClose = customer.findAccountByNumber(IBAN);
	        if (accToClose != null) {
	            customer.getAccounts().remove(accToClose);
	            return true;
	        }
	    }
	    else if(user instanceof BusinessCustomer) {
	    	BusinessCustomer bCustomer = (BusinessCustomer) user;
	        Account accToClose = bCustomer.findAccountByNumber(IBAN);
	        if (accToClose != null) {
	            bCustomer.getAccounts().remove(accToClose);
	            return true;
	        }
	    }
	    return false;
	}
}
