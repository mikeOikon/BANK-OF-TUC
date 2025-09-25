package backend.users;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public abstract class User {

	private String userID;
	private String password;
	private String email;
	private String name;
	private String surname;
	private String phoneNumber;
	private static Map<String,ArrayList<String>> users=new HashMap<String,ArrayList<String>>(); // Map to store users with userID as key and informations as value

	private ArrayList<String> informations;// List to store all users	
	
	public User(String userID, String password, String email, String name, String surname, String phoneNumber) {
		this.informations = new ArrayList<String>();
		this.informations.add(password);
		this.informations.add(email);
		this.informations.add(name);
	    this.informations.add(surname);
	    this.informations.add(phoneNumber);
	    this.users.put(userID, informations);
		
	}
	
	//na doume poies methodoi prepei na einai protected
	
	public String getUserID() {
		return userID;
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

	protected String getName() {
		return name;
	}

	protected void setName(String name) {
		this.name = name;
	}

	protected String getSurname() {
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

	protected static Map<String, ArrayList<String>> getUsers() {
		return users;
	}

	protected static void setUsers(Map<String, ArrayList<String>> users) {
		User.users = users;
	}

	protected ArrayList<String> getInformations() {
		return informations;
	}

	protected void setInformations(ArrayList<String> informations) {
		this.informations = informations;
	}

	protected void setUserID(String userID) {
		this.userID = userID;
	}

	protected void setPassword(String password) {
		this.password = password;
	}

	protected boolean login(String userID, String password) {
		return this.userID.equals(userID) && this.password.equals(password);
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

	
	
}
