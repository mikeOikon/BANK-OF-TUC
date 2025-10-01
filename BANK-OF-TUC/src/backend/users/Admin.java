package backend.users;

import java.util.Scanner;

public class Admin extends User {
	private static int customerCount = 0;
    private static int adminCount = 0;
    private static int employeeCount = 0;
    private static int auditorCount = 0;
    
	public Admin(String userID, String password, String email, String name, String surname, String phoneNumber) {
		super(userID, password, email, name, surname, phoneNumber);
	}
	public User createUserCLI() {
	    Scanner scanner = new Scanner(System.in);

	    System.out.println("What user do you want to create?\n1. Admin\n2. Customer\n3. Employer\n4. Auditor");
	    int choice = scanner.nextInt();
	    scanner.nextLine(); // consume newline

	    System.out.print("Enter password: ");
	    String password = scanner.nextLine();

	    System.out.print("Enter email: ");
	    String email = scanner.nextLine();

	    System.out.print("Enter first name: ");
	    String name = scanner.nextLine();

	    System.out.print("Enter surname: ");
	    String surname = scanner.nextLine();

	    System.out.print("Enter phone number: ");
	    String phoneNumber = scanner.nextLine();

	    // Generate a unique userId based on type
	    String userID = null;
	    User newUser = null;

	    switch (choice) {
	        case 1 : {
	            
	            userID = this.generateId(choice);
	            newUser = new Admin(userID,password, email, name, surname, phoneNumber);
	            return newUser;
	        }
	        case 2 : {
	        	 userID = this.generateId(choice);
		            newUser = new Customer(userID,password, email, name, surname, phoneNumber);
		            return newUser;
	        }
	        case 3 : {
	        	 userID = this.generateId(choice);
		            newUser = new BankEmployer(userID,password, email, name, surname, phoneNumber);
		            return newUser;
	        }
	        case 4 : {
	        	 userID = this.generateId(choice);
		            newUser = new Auditor(userID,password, email, name, surname, phoneNumber);
		            return newUser;
	        }
	        default : {
	            System.out.println("Invalid choice.");
	            return null;
	        }
	    }
	}
	
	public String generateId(int choice) {  //Genrates unique ID for each user ids are in order
	    String prefix;
	    if (choice == 1) {
	        prefix = "ADM";
	        return String.format("%s%03d", prefix, ++adminCount);
	    } else if (choice == 2) {
	        prefix = "CUS";
	        return String.format("%s%03d", prefix, ++customerCount);
	    } else if (choice == 3) {
	        prefix = "EMP";
	        return String.format("%s%03d", prefix, ++employeeCount);
	    } else if (choice == 4) {
	        prefix = "AUD";
	        return String.format("%s%03d", prefix, ++auditorCount);
	    } else {
	        throw new IllegalArgumentException("Unknown user type");
	    }

	   
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
	public void setUserId(String userID) {
		this.userID = userID;
	}
}
