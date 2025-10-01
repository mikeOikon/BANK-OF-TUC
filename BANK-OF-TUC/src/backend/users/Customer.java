package backend.users;

public class Customer extends User {

	
	public Customer(String userID, String password, String email, String name, String surname, String phoneNumber) {
		super(userID, password, email, name, surname, phoneNumber);
	}
	
	//na doume ti prepei na einai protected
	
	
	protected void createAccount() {
		
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
