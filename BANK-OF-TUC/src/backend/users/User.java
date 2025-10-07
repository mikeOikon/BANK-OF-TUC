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
	
	/*
	public ArrayList<String> getAccounts() {
		return accounts;
	}
	*/
	
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

	protected boolean login(String userID, String password) {
		//maybe use a quick pin?
		return this.username.equals(username) && this.password.equals(password);
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
    
	public void login() {
		//angel 
		int tries = 0;
		while(true) {
			System.out.println("Type username");
			String username = frontend.Main.scanner.nextLine();		
			System.out.println("Type Passward");
			String password = frontend.Main.scanner.nextLine();		
			tries++;
			if(login(username, password)) {
				System.out.println("*******Wellcome*******");
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
