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
	
	public BankSystem() {
		this.admins=new HashMap<>();
		this.customers=new HashMap<>();
		this.bankEmployers=new HashMap<>();
		this.auditors=new HashMap<>();		
		this.accounts = new ArrayList<Account>();
		
	}
	
	public void createUserCLI() {
		String userID=null;
		String password=null;
		String email=null;
		String name=null;
		String surname=null;
		String phoneNumber=null;
		Scanner scanner = new Scanner(System.in);
		System.out.println("What user do you want to create?\n1. Admin\n2. Customer\n3. Employer\n4. Auditor");
		int choice = scanner.nextInt();
		switch(choice) {
		case 1:
			User newAdmin = new Admin(userID, password, email, name, surname, phoneNumber);//create admin
			this.admins.put(userID, newAdmin);
			break;
		case 2:
			User newCustomer = new Customer(userID, password, email, name, surname, phoneNumber);//create customer
			customers.put(userID, newCustomer);
			break;
		case 3:
			User newEmployer = new BankEmployer(userID, password, email, name, surname, phoneNumber);//create employer
			bankEmployers.put(userID, newEmployer);
			break;
		case 4:
			User newAuditor = new Auditor(userID, password, email, name, surname, phoneNumber);//create auditor
			auditors.put(userID, newAuditor);
			break;
		}
		
		
	}
	
	
	
}
