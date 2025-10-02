package backend.users;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public abstract class User {
	
	protected String userID;
	protected String password;
	protected String email;
	protected String name;
	protected String surname;
	protected String phoneNumber;
	
	private ArrayList<String> accounts;	//link user to accounts 
	
	
	
	public User(String userID, String password, String email, String name, String surname, String phoneNumber) {
		this.userID = userID;
		this.password = password;
		this.email = email;
		this.name = name;
		this.surname = surname;
		this.phoneNumber = phoneNumber;
		this.accounts = new ArrayList<>();
		
	}
		
	//na doume poies methodoi prepei na einai protected
	
	public String getUserID() {
		return userID;
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
			String userID = scanner.nextLine();		
			System.out.println("Type Passward");
			String password = scanner.nextLine();		
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
