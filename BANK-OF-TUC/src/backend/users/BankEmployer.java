package backend.users;

public class BankEmployer extends User {

	public BankEmployer(String userID, String password, String email, String name, String surname, String phoneNumber) {
		super(userID, password, password, password, password, password);
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
}
