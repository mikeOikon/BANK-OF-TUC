package backend;

public class Admin extends User {

	public Admin(String userID, String password, String email, String name, String surname, String phoneNumber) {
		super(userID, password, password, password, password, password);
	}
	
	//na doume ti prepei na einai protected
	//ο αδμιν μπορει να κανει admin ή employer αλλον user (ο bank employer μονο bank employer αλλο user μαλλον)
	
	protected void manageUserAccounts() {
		
	}
	
	
	protected void acceptAccount() {
		
	}
	
	protected void rejectAccount() {
	
	}
	
	protected void deleteUserAccount() {
		
	}
	
	
	protected void updateCustomerInformation() {

	}
	
	protected void updateCustomerAccountDetails() {
		//πχ 
	}
}
