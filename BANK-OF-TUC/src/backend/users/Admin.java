package backend.users;

import java.util.Scanner;

import backend.accounts.Branch;

public class Admin extends User {
	private static int customerCount = 0;
    private static int adminCount = 0;
    private static int employeeCount = 0;
    private static int auditorCount = 0;
    
    //ισως ο admin δεν πρεπει να εχει branch
	public Admin(String userID, String username,String password, String email, String name, String surname, String phoneNumber, Branch branch) {
		super(userID, username, password, email, name, surname, phoneNumber, branch);
	}
	
	protected void promoteUser(String userId, String newRole) {
		// Logic to promote a user to a new role
		// This is a placeholder implementation
		System.out.println("User " + userId + " has been promoted to " + newRole);
	}

	protected void demoteUser(String userId, String newRole) {
		// Logic to demote a user to a new role
		// This is a placeholder implementation
		System.out.println("User " + userId + " has been demoted to " + newRole);
	}

	//na doume ti prepei na einai protected
	//ο αδμιν μπορει να κανει admin ή employer αλλον user (εφοσον αυτος δεν ειναι business customer)
	
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
	public void setUserId(String userID) {
		this.userID = userID;
	}
}
