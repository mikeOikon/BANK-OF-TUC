package backend;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import backend.accounts.Account;
import backend.users.Admin;
import backend.users.Auditor;
import backend.users.BankEmployer;
import backend.users.Customer;
import backend.users.User;

public class BankSystem {
	
	ArrayList<Account> accounts;
	private Map<String,User> admins; // Map to store admins with userID as key and informations as value
	private Map<String,User> customers; // Map to store customers with userID as key and informations as value
	private Map<String,User> bankEmployers; // Map to bankEmployers users with userID as key and informations as value
	private Map<String,User> auditors; // Map to store auditors with userID as key and informations as value
	
	private static int adminCount = 0;
	private static int customerCount = 0;
	private static int employeeCount = 0;
	private static int auditorCount = 0;
	
	public BankSystem() {
		this.admins=new HashMap<>();
		this.customers=new HashMap<>();
		this.bankEmployers=new HashMap<>();
		this.auditors=new HashMap<>();		
		this.accounts = new ArrayList<Account>();
		
	}
	
	public void createUserCLI() {
		Scanner scanner = new Scanner(System.in);
		System.out.println("Type password: ");	//maybe should be hidden and have some rules
		String password = scanner.nextLine();
		System.out.println("Type email: ");
		String email = scanner.nextLine();
		System.out.println("Type name: ");
		String name = scanner.nextLine();
		System.out.println("Type surname: ");
		String surname = scanner.nextLine();
		System.out.println("Type phone number: ");
		String phoneNumber = scanner.nextLine();
		String userID = generateId(2); //2 for customer
		User newCustomer = new Customer(userID, password, email, name, surname, phoneNumber);//create customer
		customers.put(userID, newCustomer);
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
	
	
}
