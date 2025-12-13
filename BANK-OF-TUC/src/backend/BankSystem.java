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
import backend.accounts.FixedTermAccount;
import backend.accounts.SavingsAccount;
import backend.accounts.TransactionalAccount;
import backend.transactions.Transaction;
import backend.transactions.TransactionFactory;
import backend.accounts.AccountFactory;
import backend.users.Admin;
import backend.users.Auditor;
import backend.users.BankEmployer;
import backend.users.Customer;
import backend.users.User;
import backend.users.UserBuilder;
import backend.users.UserFactory;
import backend.users.ΒusinessCustomer;
import services.UserManager;
import services.Command;
import services.CreateUserCommand;
import types.UserType;
//import jdk.internal.org.jline.terminal.TerminalBuilder.SystemOutput;

public class BankSystem {
	
	//ArrayList<Account> accounts;    //να δουμε αν χρειαζεται ( η τράπεζα να ξερει για τους λογαριασμούς ή οι χρήστες);
	private static volatile BankSystem instance;
	private Map<String, Branch> branches;
	private Map<String,ΒusinessCustomer> businessCustomers; // Map to store accounts with IBAN as key and account informations as value
	private Map<String,Admin> admins; // Map to store admins with userID as key and informations as value
	private Map<String,Customer> customers; // Map to store customers with userID as key and informations as value
	private Map<String,BankEmployer> bankEmployers; // Map to bankEmployers users with userID as key and informations as value
	private Map<String,Auditor> auditors; // Map to store auditors with userID as key and informations as value
	private BankAccount bankAccount; // η τράπεζα έχει και έναν λογαριασμό για τις προμήθειες κλπ
	private transient UserManager userManager;
	private Map<String, Map<String, ? extends User>> userMaps;
	private Map<String,User> usersByUsername; // Map to find users by username during login
	
	private  int adminCount = 0;
	private  int customerCount = 0;
	private  int employeeCount = 0;
	private  int auditorCount = 0;
	
	
    private static final String DATA_FILE = "data/bankSystem.json";
    
    private transient Gson gson;
	
	private BankSystem() {

		this.gson = GsonConfig.build();   
		this.admins=new HashMap<>();
		this.customers=new HashMap<>();
		this.bankEmployers=new HashMap<>();
		this.auditors=new HashMap<>();		
		this.businessCustomers=new HashMap<>();
		this.bankAccount = new BankAccount("BANK001", Branch.getDefaultBranch()); //default bank account(TUC)
		this.userManager=new UserManager();
		this.userMaps = new HashMap<>();
		  userMaps.put("ADM", admins);
		  userMaps.put("CUS", customers);
		  userMaps.put("EMP", bankEmployers);
		  userMaps.put("AUD", auditors);
		  userMaps.put("BUS", businessCustomers);
		this.usersByUsername = new HashMap<>();
		
		AccountFactory accountFactory = new AccountFactory();
	}
		
	public void getAllCustomers() {
		for(User user : this.customers.values()) {
			Customer customer = (Customer) user;
			System.out.println("Customer ID: " + customer.getUserID() + ", Name: " + customer.getName() + " " + customer.getSurname());
		}
	}
	
	public void saveAllData() {
		if (this.gson == null) {
			this.gson = GsonConfig.build();
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
	
	public static BankSystem getInstance() {
	    if (instance == null) {
	        synchronized (BankSystem.class) {
	            if (instance == null) {
	                instance = loadFromFileInternal();
	            }
	        }
	    }
	    return instance;
	}
	
	public static BankSystem loadFromFileInternal() {

	    File file = new File(DATA_FILE);

	    if (!file.exists() || file.length() == 0) {
	        System.out.println("[BankSystem] No saved data found. Creating new system.");
	        return new BankSystem();
	    }

	    Gson gson = GsonConfig.build();
	    BankSystem loaded;

	    try (FileReader reader = new FileReader(file)) {
	        loaded = gson.fromJson(reader, BankSystem.class);
	        System.out.println("[BankSystem] ✅ Data loaded successfully.");
	    }
	    catch (Exception e) {
	        System.err.println("[BankSystem] ⚠️ Failed to load data: " + e.getMessage());
	        return new BankSystem();
	    }

	    // ΦΤΙΑΧΝΟΥΜΕ ΝΕΟ ΣΥΣΤΗΜΑ ΜΕ ΣΩΣΤΟ GSON
	    BankSystem system = new BankSystem();
	    system.gson = gson;

	    // ΚΑΙ ΑΝΤΙΓΡΑΦΟΥΜΕ ΟΛΑ ΤΑ LOADED FIELDS
	    system.admins = loaded.admins;
	    system.customers = loaded.customers;
	    system.bankEmployers = loaded.bankEmployers;
	    system.auditors = loaded.auditors;
	    system.businessCustomers = loaded.businessCustomers;
	    system.bankAccount = loaded.bankAccount;

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
	
	public void addUser(User user) {
		switch(user.getUserType()) {
			case ADMIN:
				this.admins.put(user.getUserID(), (Admin) user);
				break;
			case CUSTOMER:
				this.customers.put(user.getUserID(), (Customer) user);
				break;
			case EMPLOYEE:
				this.bankEmployers.put(user.getUserID(), (BankEmployer) user);
				break;
			case AUDITOR:
				this.auditors.put(user.getUserID(), (Auditor) user);
				break;
			case BUSINESSCUSTOMER:
				this.businessCustomers.put(user.getUserID(), (ΒusinessCustomer) user);
				break;
			default:
				throw new IllegalArgumentException("Invalid user type: " + user.getUserType());
		}
		
		usersByUsername.put(user.getUsername(), user);
	}
	
	
	public void removeUser(String userId) {
	String prefix = userId.substring(0, 3);
	Map<String, ? extends User> userMap = userMaps.get(prefix);

	if (userMap != null) {	
		User removed= userMap.remove(userId);
		if (removed != null) {
			usersByUsername.remove(removed.getUsername());	
			System.out.println("User " + userId + " removed successfully.");
		} else {
			System.out.println("User " + userId + " not found.");
		}
	} else {
		System.out.println("Invalid user ID prefix: " + prefix);
	}
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
				String userID = generateId(UserType.CUSTOMER); //2 for customer
				UserBuilder userBuilder = new UserBuilder();
			try {
				userBuilder.withUsername(username)
						   .withPassword(PasswordHasher.hash(password))
						   .withEmail(email)
						   .withName(name)
						   .withSurname(surname)
						   .withPhoneNumber(phoneNumber)
						   .withBranch(Branch.getDefaultBranch());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();		
			}
				User newCustomer = UserFactory.createUser(UserType.CUSTOMER,userID,userBuilder);//create customer
				Command create= new CreateUserCommand(newCustomer);				
				userManager.execute(create);
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
				String businessUserID = generateId(UserType.BUSINESSCUSTOMER); //businessCustomer for businessCustomer (different from simple customer)
				UserBuilder businessUserBuilder = new UserBuilder();
			try {
				businessUserBuilder.withUsername(businessUsername)
								   .withPassword(PasswordHasher.hash(businessPassword))
								   .withEmail(businessEmail)
								   .withName(businessName)
								   .withSurname(repname)
								   .withPhoneNumber(businessPhoneNumber)
								   .withBranch(Branch.getDefaultBranch());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
				User newBusinessCustomer = UserFactory.createUser(UserType.BUSINESSCUSTOMER,businessUserID,businessUserBuilder);//create customer
				Command businessCreate= new CreateUserCommand(newBusinessCustomer);				
				userManager.execute(businessCreate);
				return newBusinessCustomer;
			default:
				System.out.println("Invalid choice. Please select 1 or 2.");
				return null;
		}
	}
	
	public User loginCLI() {
	    Scanner scanner = frontend.Main.scanner;
	    int attempts = 0;

	    while (true) {
	        System.out.print("Username: ");
	        String username = scanner.nextLine();

	        System.out.print("Password: ");
	        String password = scanner.nextLine();

	        User user = findUserByUsername(username);

	        attempts++;
	        if (user != null) {
	            try {
	                if (PasswordHasher.verify(password, user.getPassword())) {
	                    System.out.println("******* Welcome " + user.getName() + " *******");
	                    return user; // successful login
	                } else {
	                    System.out.println("Incorrect password.");
	                }
	            } catch (Exception e) {
	                System.err.println("Error verifying password: " + e.getMessage());
	            }
	        } else {
	            System.out.println("Username not found.");
	        }

	        // Handle failed attempts with incremental wait time
	        if (attempts % 3 == 0) {
	            int waitMinutes = attempts / 3;
	            System.out.println("Too many failed attempts. Wait " + waitMinutes + " minute(s) before retrying.");
	            try {
	                Thread.sleep(waitMinutes * 60 * 1000L); // convert minutes to milliseconds
	            } catch (InterruptedException e) {
	                e.printStackTrace();
	            }
	        }
	    }
	}

	// Helper method: finds user across all maps
	private User findUserByUsername(String username) {
	    for (Map<String, ? extends User> map : userMaps.values()) {
	        for (User u : map.values()) {
	            if (u.getUsername().equals(username)) {
	                return u;
	            }
	        }
	    }
	    return null;
	}
	public String generateId(UserType type) {  //Genrates unique ID for each user ids are in order
	    String prefix;
	    if (type == UserType.ADMIN) {
	        prefix = "ADM";
	        return String.format("%s%03d", prefix, ++adminCount);
	    } else if (type == UserType.CUSTOMER) {
	        prefix = "CUS";
	        return String.format("%s%03d", prefix, ++customerCount);
	    } else if (type == UserType.EMPLOYEE) {
	        prefix = "EMP";
	        return String.format("%s%03d", prefix, ++employeeCount);
	    } else if (type == UserType.AUDITOR) {
	        prefix = "AUD";
	        return String.format("%s%03d", prefix, ++auditorCount);
	    } else if (type == UserType.BUSINESSCUSTOMER) {
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
	
	public void transferMoney(Customer customer) {

	    System.out.println("Type the account number you want to transfer from: ");
	    String fromAccountNumber = frontend.Main.scanner.nextLine();

	    System.out.println("Type the account number you want to transfer to: ");
	    String toAccountNumber = frontend.Main.scanner.nextLine();

	    System.out.println("Type the amount you want to transfer: ");
	    double amount = frontend.Main.scanner.nextDouble();
	    frontend.Main.scanner.nextLine(); // consume newline

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

	    if (!(fromAccount instanceof TransactionalAccount)) {
	        System.out.println("The source account does not support transactions.");
	        return;
	    }

	    if (fromAccount.getBalance() < amount) {
	        System.out.println("Insufficient funds.");
	        return;
	    }

	    if (toAccount instanceof FixedTermAccount) {
	        System.out.println("The destination account does not support transactions.");
	        return;
	    }

	    // 💰 Εφαρμογή χρέωσης σε διαφορετική τράπεζα (προμήθεια 1€)
	    if (!fromAccount.getBranch().getBankCode().equals(toAccount.getBranch().getBankCode())) {

	        if (fromAccount.getBalance() < amount + 1) {
	            System.out.println("Insufficient funds to cover transfer fee.");
	            return;
	        }

	        fromAccount.setBalance(fromAccount.getBalance() - (amount + 1));
	        toAccount.setBalance(toAccount.getBalance() + amount);
	        bankAccount.receivePayment(1.0);
	    } else {
	        fromAccount.setBalance(fromAccount.getBalance() - amount);
	        toAccount.setBalance(toAccount.getBalance() + amount);
	    }

	    // ✅ Χρησιμοποιούμε πλέον FACTORY
	    Transaction transferTx = TransactionFactory.createTransaction(fromAccount, toAccount, amount);

	    fromAccount.getTransactions().add(transferTx);
	    toAccount.getTransactions().add(transferTx);

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

