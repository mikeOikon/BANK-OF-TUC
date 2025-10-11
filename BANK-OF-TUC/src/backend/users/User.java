package backend.users;

import backend.accounts.Branch;

public abstract class User {
	
	protected String username;
	protected String userID;
	protected String password;
	protected String email;
	protected String name;
	protected String surname;
	protected String phoneNumber;
	protected Branch branch;	//link user to branch
	
	
	
	public User(String userID, String username, String password, String email, String name, String surname, String phoneNumber, Branch branch) {
		this.userID = userID;
		this.username = username;
		this.password = password;
		this.email = email;
		this.name = name;
		this.surname = surname;
		this.phoneNumber = phoneNumber;
		this.branch = branch;
		
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
	
	private String getPassword() {
		return password;
	}
	
	
	
	protected String getEmail() {
		return email;
	}

	protected void setEmail(String email) {
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

	protected String getPhoneNumber() {
		return phoneNumber;
	}

	protected void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}



	protected void setPassword(String password) {
		this.password = password;
	}

	
	protected void logout() {
		// Implement logout functionality
	}
	
	protected void changePassword(String newPassword) {
		this.password = newPassword;
	}
	
}
