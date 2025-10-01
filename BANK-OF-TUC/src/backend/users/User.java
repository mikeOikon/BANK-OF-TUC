package backend.users;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public abstract class User {

	private String userID;
	private String password;
	private String email;
	private String name;
	private String surname;
	private String phoneNumber;
	private static Map<String,ArrayList<String>> users=new HashMap<String,ArrayList<String>>(); // Map to store users with userID as key and informations as value
	//private ArrayList<>	//link user to accounts 
	
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
		//maybe use a quick pin?
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

	protected void login() {
		//angel 
		int tries = 0;
		Scanner scanner = new Scanner(System.in);
		while(true) {
			System.out.println("Type userID");
			String userID = scanner.nextLine();		//takes String from scanner?
			System.out.println("Type Passward");
			String password = scanner.nextLine();		//takes String from scanner?
			tries++;
			if(login(userID, password)) {
				System.out.println("*******Wellcome*******");
				scanner.close();		//maybe not close scanner here
				return;
			}
			else {
				System.out.println("Wrong username or password. Try again!");
				if(tries % 3 == 0) {
					int waitMinutes = tries / 3; // Calculate wait time in minutes
					System.out.print("Try again in ");
					System.out.print(waitMinutes);
					System.out.println(" minutes");	//makes trying available after +1 minute every 3 attempts
					try {
	                    Thread.sleep(waitMinutes * 60 * 1000); // μετατρέπει λεπτά σε ms and waits for that time
	                } catch (InterruptedException e) {
	                    e.printStackTrace();
	                }
				}
			}		
		}
	}
	
}
