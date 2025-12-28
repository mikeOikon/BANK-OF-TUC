package backend.users;

import java.util.ArrayList;

import backend.Branch;
import backend.FileLogger;
import backend.PasswordHasher;
import backend.accounts.Account;
import behaviors.UserBehavior;
import types.UserType;

public abstract class User {
	
	protected String username;
	protected String userID;
	protected String password;
	protected String email;
	protected String name;
	protected String surname;
	protected String phoneNumber;
	protected Branch branch;	//link user to branch
	protected transient UserBehavior userBehavior; // Bridge  pattern for user-specific behaviors
	protected String AFM;

	public User(String userID, String username, String password, String name, String surname, Branch branch, String AFM) {
		this.userID = userID;
		this.username = username;
		this.password = password;
		this.name = name;
		this.surname = surname;
		this.AFM=AFM;
		this.branch = branch;	
	}
	
	public ArrayList<Account> getAccounts(){
		return null; //to be overridden
	}
	
	public String getAFM() {
		return AFM;
	}

	public void setAFM(String aFM) {
		AFM = aFM;
	}

	public void setBehavior(UserBehavior behavior) {
	    this.userBehavior = behavior;
	}
	
	public Account createAccount(int choice) {
		return null;
	}
	
	public boolean canViewAccounts() {
		// TODO Auto-generated method stub
		return userBehavior.canViewAccounts();
	}

	
	public boolean canTransferMoney() {
		// TODO Auto-generated method stub
		return userBehavior.canTransferMoney();
	}

	public boolean canViewTransactionsHistory() {
		// TODO Auto-generated method stub
		return userBehavior.canViewTransactionsHistory();
	}

	public boolean canRemoveUsers() {
	    return userBehavior.canRemoveUsers();
	}

	public boolean canViewAllAccounts() {
	    return userBehavior.canViewAllAccounts();
	}
	

	public boolean canViewAllTransactionsHistory() {
		// TODO Auto-generated method stub
		return userBehavior.canViewAllTransactionsHistory();
	}

	public boolean canCreateAuditor() {
		// TODO Auto-generated method stub
		return userBehavior.canCreateAuditor();
	}

	public boolean canPromoteUser() {
		// TODO Auto-generated method stub
		return userBehavior.canPromoteUser();
	}

	public boolean canDemoteUser() {
		// TODO Auto-generated method stub
		return userBehavior.canDemoteUser();
	}


	public boolean canAssistUsers() {
		// TODO Auto-generated method stub
		return userBehavior.canAssistUsers();
	}
	
	public boolean canOpenTicket() {
		return userBehavior.canOpenTicket();
	}

	
	public UserType getUserType() {
		return null; //to be overridden
	}
		
	//na doume poies methodoi prepei na einai protected
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getUserID() {
		return userID;
	}
	
	public Branch getBranch() {
		return branch;
	}
	
	public void setUserID(String userID) {
		this.userID = userID;
	}
	
	public String getPassword() {
		return password;
	}
	
	
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}

	protected void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	protected void setSurname(String surname) {
		this.surname = surname;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}



	public void setPassword(String password) {
		this.password = password;
	}

	public boolean login(String username, String password) {
		//maybe use a quick pin?
		FileLogger logger= FileLogger.getInstance();
		try {
			
			return this.username.equals(username) && this.password.equals(PasswordHasher.hash(password));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return false;
		}
	}
	
	protected void logout() {
		// Implement logout functionality
	}
	
	protected void changePassword(String newPassword) {
		this.password = newPassword;
	}
	
	//protected void resetPassword() {
		// Implement reset password functionality
	
	//}
	
	protected void deleteAccount() {
		// Implement delete account functionality
	}

	public abstract Object getFullName();

	public Account getPrimaryAccount() {
		return null;
	}

	
}
