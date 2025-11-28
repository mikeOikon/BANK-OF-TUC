package backend;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


import backend.accounts.Account;
import backend.accounts.BankAccount;
import backend.accounts.Branch;
import backend.accounts.FixedTermAccount;
import backend.accounts.Transaction;
import backend.accounts.TransactionalAccount;
import backend.users.Admin;
import backend.users.Auditor;
import backend.users.BankEmployer;
import backend.users.Customer;
import backend.users.User;
import backend.users.ΒusinessCustomer;
//import jdk.internal.org.jline.terminal.TerminalBuilder.SystemOutput;

public class BankSystem {
	//mono aek 
	//ArrayList<Account> accounts;    //να δουμε αν χρειαζεται ( η τράπεζα να ξερει για τους λογαριασμούς ή οι χρήστες);
	private Map<String, Branch> branches;
	private Map<String,ΒusinessCustomer> businessCustomers; // Map to store accounts with IBAN as key and account informations as value
	private Map<String,Admin> admins; // Map to store admins with userID as key and informations as value
	private Map<String,Customer> customers; // Map to store customers with userID as key and informations as value
	private Map<String,BankEmployer> bankEmployers; // Map to bankEmployers users with userID as key and informations as value
	private Map<String,Auditor> auditors; // Map to store auditors with userID as key and informations as value
	private BankAccount bankAccount; // η τράπεζα έχει και έναν λογαριασμό για τις προμήθειες κλπ
	
	private  int adminCount = 0;
	private  int customerCount = 0;
	private  int employeeCount = 0;
	private  int auditorCount = 0;
	
	
    private static final String DATA_FILE = "data/bankSystem.json";
    
    private transient Gson gson;
	
	public BankSystem() {
		
		gson = new GsonBuilder().setPrettyPrinting().create();

		this.admins=new HashMap<>();
		this.customers=new HashMap<>();
		this.bankEmployers=new HashMap<>();
		this.auditors=new HashMap<>();		
		this.businessCustomers=new HashMap<>();
		//this.accounts = new ArrayList<Account>();
		this.bankAccount = new BankAccount("BANK001", Branch.getDefaultBranch()); //default bank account(TUC)
		//this.branches = new HashMap<>();
        //Branch mainBranch = new Branch(bankCode, branchCode);
        //branches.put(branchCode, mainBranch); // χρησιμοποιούμε branchCode ως key
        
	}
		
	public void getAllCustomers() {
		for(User user : this.customers.values()) {
			Customer customer = (Customer) user;
			System.out.println("Customer ID: " + customer.getUserID() + ", Name: " + customer.getName() + " " + customer.getSurname());
		}
	}
	
	
	
	public void saveAllData() {
		if (this.gson == null) {
		    gson = new GsonBuilder().setPrettyPrinting().create();
		}
		
        try {
        
            File dir = new File("data");
            if (!dir.exists()) dir.mkdir();

            try (FileWriter writer = new FileWriter(DATA_FILE)) {
                this.gson.toJson(this, writer); // serialize entire BankSystem
                writer.flush();
            }
            System.out.println("[BankSystem] ✅ Data saved successfully.");
            System.out.println("[BankSystem] Saving JSON at: " + new File(DATA_FILE).getAbsolutePath());

        } catch (IOException e) {
            System.err.println("[BankSystem] ❌ Failed to save data: " + e.getMessage());
        }
    }


	public static BankSystem loadFromFile() {
	    File file = new File(DATA_FILE);
	    BankSystem system;
	    Gson gson = new GsonBuilder().setPrettyPrinting().create();

	    if (!file.exists() || file.length() == 0) {
	        system = new BankSystem();
	        System.out.println("[BankSystem] No saved data found. Creating new system.");
	    } else {
	        try (FileReader reader = new FileReader(file)) {
	            system = gson.fromJson(reader, BankSystem.class);
	            System.out.println("[BankSystem] ✅ Data loaded successfully.");
	            system.getAllCustomers(); // Display loaded customers for verification
	             
	        } catch (Exception e) {
	            System.err.println("[BankSystem] ⚠️ Failed to load data: " + e.getMessage());
	            system = new BankSystem();
	        }
	    }

	    system.gson = gson;
	    return system;
	}
	//να γινεται save οταν γινεται καποια αλλαγη και οταν κλεινει το προγραμμα
	 public void shutdown() {
	        this.saveAllData();
	        System.out.println("[BankSystem] System shutting down, data persisted.");
	    }
	public Branch getBranch(String branchCode) {
        return branches.get(branchCode);
    }
		
	public User createUserCLI() {
		System.out.println("Select user type to create: Type 1 for Personal Customer, Type 2 for Business Customer");
		int choice = frontend.Main.scanner.nextInt();
		frontend.Main.scanner.nextLine(); // Consume newline
		switch (choice) {
			case 1:
				String username;
				String password;
				String email;
				String name;
				String surname;
				String phoneNumber;
				boolean valid;
				do {
					valid = true;
					System.out.println("Type Username: ");
					username = frontend.Main.scanner.nextLine();
					if (username.isEmpty()) {
						System.out.println("Username cannot be empty.");
						valid = false;
						continue;
					}
					if (!username.matches("[a-zA-Z0-9_]+")) {
						System.out.println("Okay, username can contain only characteres allowed (letters, numbers and underscores)."); // I want to rewrite a Username
						valid = false;
						continue;
					}
					for (User user : customers.values()) {
						if (user.getUsername().equals(username)) {
							System.out.println("Username is already taken. Please choose another one.");
							valid = false;
							break;
						}
					}
				} while (!valid); //loop until valid username is provided

				System.out.println("Type password: ");//maybe should be hidden and have some rules
				//I want an if statement to check if password has at least 8 characters, one uppercase letter, one lowercase letter and one number
				do {
					valid = true;
					password = frontend.Main.scanner.nextLine();
					if (password.isEmpty()) {
						System.out.println("Password cannot be empty.");
						valid = false;
						continue;
					}
					if (password.length() < 8) {
						System.out.println("Password must be at least 8 characters long.");
						valid = false;
						continue;
					}
					if (!password.matches(".*[A-Z].*")) {
						System.out.println("Password must contain at least one uppercase letter.");
						valid = false;
						continue;
					}
					if (!password.matches(".*[a-z].*")) {
						System.out.println("Password must contain at least one lowercase letter.");
						valid = false;
						continue;
					}
					if (!password.matches(".*\\d.*")) {
						System.out.println("Password must contain at least one number.");
						valid = false;
						continue;
					}
					if (password.contains(" ")) {
						System.out.println("Password cannot contain spaces.");
						valid = false;
					}
				} while (!valid); //loop until valid password is provided

				System.out.println("Type email: ");
				//email validation
				do {
					valid = true;
					email = frontend.Main.scanner.nextLine();
					if (email.isEmpty()) {
						System.out.println("Email cannot be empty.");
						valid = false;
						continue;
					}
					if (!email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
						System.out.println("Invalid email format. Please try again.");
						valid = false;
					}
				} while (!valid); //loop until valid email is provided

				System.out.println("Type name: ");
				do {
					valid = true;
					name = frontend.Main.scanner.nextLine();
					if (name.isEmpty()) {
						System.out.println("Name cannot be empty.");
						valid = false;
						continue;
					}
					if (!name.matches("[a-zA-Z]+")) {
						System.out.println("Name can contain only letters. Please try again.");
						valid = false;
					}
				} while (!valid); //loop until valid name is provided

				System.out.println("Type surname: ");
				do {
					valid = true;
					surname = frontend.Main.scanner.nextLine();
					if (surname.isEmpty()) {
						System.out.println("Surname cannot be empty.");
						valid = false;
						continue;
					}
					if (!surname.matches("[a-zA-Z' -]+")) {
						System.out.println("Surname can contain only letters the -,' special characters and space. Please try again.");
						valid = false;
					}
				} while (!valid); //loop until valid surname is provided


				System.out.println("Type phone number: ");
				do {
					valid = true;
					phoneNumber = frontend.Main.scanner.nextLine();
					if (phoneNumber.isEmpty()) {
						System.out.println("Phone number cannot be empty.");
						valid = false;
						continue;
					}
					if (!phoneNumber.matches("^[0-9]{10}$")) {
						System.out.println("Invalid phone number format. Please try again.");
						valid = false;
					}
				} while (!valid); //loop until valid phone number is provided
				//customers are created with the main branch, if we want to create customers with different branches we need to change this
				String userID = generateId(2); //2 for customer
				User newCustomer = new Customer(userID, username, password, email, name, surname, phoneNumber, Branch.getDefaultBranch());//create customer
				this.customers.put(userID, (Customer) newCustomer);
				return newCustomer;
			case 2:
				String businessUsername;
				String businessPassword;
				String businessEmail;
				String businessName;
				String repname;
				String businessPhoneNumber;

				System.out.println("Type Username: ");
				boolean businessValid;
				do {
					businessValid = true;
					businessUsername = frontend.Main.scanner.nextLine();
					if (businessUsername.isEmpty()) {
						System.out.println("Username cannot be empty.");
						businessValid = false;
						continue;
					}
					if (!businessUsername.matches("[a-zA-Z0-9_]+")) {
						System.out.println("Okay, username can contain only characteres allowed (letters, numbers and underscores)."); // I want to rewrite a Username
						businessValid = false;
						continue;
					}
					for (User user : businessCustomers.values()) {
						if (user.getUsername().equals(businessUsername)) {
							System.out.println("Username is already taken. Please choose another one.");
							businessValid = false;
							break;
						}
					}
				} while (!businessValid); //loop until valid username is provided
				System.out.println("Type password: ");    //maybe should be hidden and have some rules
				do {
					businessValid = true;
					businessPassword = frontend.Main.scanner.nextLine();
					if (businessPassword.isEmpty()) {
						System.out.println("Password cannot be empty.");
						businessValid = false;
						continue;
					}
					if (businessPassword.length() < 8) {
						System.out.println("Password must be at least 8 characters long.");
						businessValid = false;
						continue;
					}
					if (!businessPassword.matches(".*[A-Z].*")) {
						System.out.println("Password must contain at least one uppercase letter.");
						businessValid = false;
						continue;
					}
					if (!businessPassword.matches(".*[a-z].*")) {
						System.out.println("Password must contain at least one lowercase letter.");
						businessValid = false;
						continue;
					}
					if (!businessPassword.matches(".*\\d.*")) {
						System.out.println("Password must contain at least one number.");
						businessValid = false;
						continue;
					}
					if (businessPassword.contains(" ")) {
						System.out.println("Password cannot contain spaces.");
						businessValid = false;
					}
				} while (!businessValid); //loop until valid password is provided
				System.out.println("Type business email: ");
				do {
					businessValid = true;
					businessEmail = frontend.Main.scanner.nextLine();
					if (businessEmail.isEmpty()) {
						System.out.println("Email cannot be empty.");
						businessValid = false;
						continue;
					}
					if (!businessEmail.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
						System.out.println("Invalid email format. Please try again.");
						businessValid = false;
					}
				} while (!businessValid); //loop until valid email is provided
				System.out.println("Type business name: ");
				do {
					businessValid = true;
					businessName = frontend.Main.scanner.nextLine();
					if (businessName.isEmpty()) {
						System.out.println("Business name cannot be empty.");
						businessValid = false;
						continue;
					}
					if (!businessName.matches("[a-zA-Z0-9 '&-]+")) {
						System.out.println("Business name can contain only letters, numbers and the special characters -,&,' and space. Please try again.");
						businessValid = false;
					}
				} while (!businessValid); //loop until valid business name is provided
				System.out.println("Type representative name: ");
				do {
					businessValid = true;
					repname = frontend.Main.scanner.nextLine();
					if (repname.isEmpty()) {
						System.out.println("Representative name cannot be empty.");
						businessValid = false;
						continue;
					}
					if (!repname.matches("[a-zA-Z]+")) {
						System.out.println("Representative name can contain only letters. Please try again.");
						businessValid = false;
					}
				} while (!businessValid); //loop until valid representative name is provided
				System.out.println("Type business phone number: ");
				do {
					businessValid = true;
					businessPhoneNumber = frontend.Main.scanner.nextLine();
					if (businessPhoneNumber.isEmpty()) {
						System.out.println("Phone number cannot be empty.");
						businessValid = false;
						continue;
					}
					if (!businessPhoneNumber.matches("^[0-9]{10}$")) {
						System.out.println("Invalid phone number format. Please try again.");
						businessValid = false;
					}
				} while (!businessValid); //loop until valid phone number is provided
				//business customers are created with the main branch, if we want to create business customers with different branches we need to change this
				String businessUserID = generateId(5); //5 for businessCustomer (different from simple customer)
				User newBusinessCustomer = new ΒusinessCustomer(businessUserID, businessUsername, businessPassword, businessEmail, businessName, repname, businessPhoneNumber, Branch.getDefaultBranch());//create business customer
				this.businessCustomers.put(businessUserID, (ΒusinessCustomer) newBusinessCustomer);
				return newBusinessCustomer;
			default:
				System.out.println("Invalid choice. Please select 1 or 2.");
				return null;
		}
	}
	
	//na ginetai elegxos an to username uparxei hdh
	/*protected void login() {
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
	
	protected boolean login(String username, String password) {
		//maybe use a quick pin?
		for
		return this.username.equals(username) && this.password.equals(password);
	}*/
	
	
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
	    } else if (choice == 5) {
	        prefix = "BUS";
	        return String.format("%s%03d", prefix, ++customerCount); //business customer count is same as customer count
	    } else {
	        throw new IllegalArgumentException("Unknown user type");
	    }

	   
	}	
	
	public Account getAccountbyNumber(String accountNumber) {	//method to find account by account number (used in transfer money)
		for(User user : customers.values()) {
			Customer customer = (Customer) user;
			Account foundAccount = customer.findAccountByNumber(accountNumber);
			if(foundAccount != null)
				return foundAccount;
		}
		return null; // Account not found
	}
	
	public void transferMoney(Customer customer) {	//method is here because bankSystem can see all Accounts
		//select account to transfer from and account to transfer to
		//ισως να πηγαινει μηνυμα στην τραπεζα στις μεταφορες
		System.out.println("Type the account number you want to transfer from: ");
		String fromAccountNumber = frontend.Main.scanner.nextLine();
		System.out.println("Type the account number you want to transfer to: ");
		String toAccountNumber = frontend.Main.scanner.nextLine();
		System.out.println("Type the amount you want to transfer: ");
		double amount = frontend.Main.scanner.nextDouble();
		frontend.Main.scanner.nextLine(); // Consume newline(consumes the newline character)
		Account fromAccount = customer.findAccountByNumber(fromAccountNumber);
		Account toAccount = getAccountbyNumber(toAccountNumber);
		if (amount <= 0) {
			System.out.println("Amount must be positive."); 
			return;
		}
		if (fromAccount == null || toAccount == null) {	
			System.out.println("One or both of the account numbers are invalid.");
			return;
		}	
		if (fromAccount.equals(toAccount)) {
			System.out.println("You cannot transfer money to the same account.");
			return;
		}
		if(!(fromAccount instanceof TransactionalAccount)) {	//γινεται ελεγχος αν ο λογαριασμος υποστηριζει συναλαγες
			System.out.println("The source account does not support transactions.");
			return;
		}
		if (fromAccount.getBalance() < amount) {
			System.out.println("Insufficient funds in the source account.");
			return;
		}
		if(toAccount instanceof FixedTermAccount) {	//γινεται ελεγχος αν ο λογαριασμος μπορει να δεχεται χρηματα
			System.out.println("The destination account does not support transactions.");
			return;
		}
		if (!fromAccount.getBranch().getBankCode().equals(toAccount.getBranch().getBankCode())) { // σε transfer σε αλλη τραπεζα προμηθεια 1 euro σε παραληπτη
			if(fromAccount.getBalance() < amount + 1) {
				System.out.println("Insufficient funds in the source account to cover the transfer fee.");
				return;
			}
			fromAccount.setBalance(fromAccount.getBalance() - (amount + 1));
			toAccount.setBalance(toAccount.getBalance() + amount);
			bankAccount.receivePayment(1.0); // η τράπεζα παίρνει 1 euro προμήθεια
		}
		else {
			fromAccount.setBalance(fromAccount.getBalance() - amount);
			toAccount.setBalance(toAccount.getBalance() + amount);
		}
		//update transaction history for both accounts
		Transaction transfer = new Transaction("transfer", amount, fromAccount.getIBAN(), toAccount.getIBAN());
		fromAccount.getTransactions().add(transfer);
		toAccount.getTransactions().add(transfer);
		
		System.out.println("Transfer successful. New balance of source account: " + fromAccount.getBalance());	
	}

	//mathod to get all accounts in the bank system (used by auditor) 
	public List<Account> getAllAccounts() {
		List<Account> allAccounts = new ArrayList<>();
		for(User user : customers.values()) {
			Customer customer = (Customer) user;
			allAccounts.addAll(customer.getAccounts());
		}
		return List.copyOf(allAccounts);
	}

	//method to get transaction history of Customer(used by auditor)
	//sosssssssssssssss gia business customer na gyrnaei ta transaction tou business account
	public void viewTransactionsByCustomer(String customerID) {
		User user = customers.get(customerID); //check if personal customer
		if (user == null) { 
			user = businessCustomers.get(customerID);	//check if business customer
		}
		if (user == null || !(user instanceof Customer)) { // if not found or not a customer
			System.out.println("No customer found with ID: " + customerID);
			return;
		}
		if(user instanceof ΒusinessCustomer) {
			System.out.println("Viewing transactions for Business Customers is not yet implemented.");	//
			return;
		}
		Customer customer = (Customer) user;
		List<Transaction> allTransactions = new ArrayList<>();
		for (Account acc : customer.getAccounts()) {
		    allTransactions.addAll(acc.getTransactions());
		}
		if (allTransactions.isEmpty()) {
	        System.out.println("No transactions found for customer " + customerID);
	        return;
	    }
		
		System.out.println("Transaction history for customer " + customerID + ":");
		for (Transaction t : allTransactions) {
		    System.out.println(t);
		}
		
	}
	
	//na ftiaxtei methodos gia plhromes klp
	
	
}

