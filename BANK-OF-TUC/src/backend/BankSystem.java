package backend;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

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
import backend.users.businessCustomer;

public class BankSystem {
	
	//ArrayList<Account> accounts;    //να δουμε αν χρειαζεται ( η τράπεζα να ξερει για τους λογαριασμούς ή οι χρήστες);
	private Map<String, Branch> branches;
	private Map<String,User> admins; // Map to store admins with userID as key and informations as value
	private Map<String,User> customers; // Map to store customers with userID as key and informations as value
	private Map<String,User> bankEmployers; // Map to bankEmployers users with userID as key and informations as value
	private Map<String,User> auditors; // Map to store auditors with userID as key and informations as value
	private BankAccount bankAccount; // η τράπεζα έχει και έναν λογαριασμό για τις προμήθειες κλπ
	
	private static int adminCount = 0;
	private static int customerCount = 0;
	private static int employeeCount = 0;
	private static int auditorCount = 0;
	
	private static final String bankCode = "021"; // κωδικός τράπεζας Bank of TUC
	private static final String branchCode = "0021"; // κωδικός καταστήματος (υποκαταστήματος) Main Branch
	
	public BankSystem() {
		this.admins=new HashMap<>();
		this.customers=new HashMap<>();
		this.bankEmployers=new HashMap<>();
		this.auditors=new HashMap<>();		
		//this.accounts = new ArrayList<Account>();
		this.bankAccount = new BankAccount("BANK001", new Branch(bankCode, branchCode)); //BAMK001 is the bank account number
		this.branches = new HashMap<>();
        Branch mainBranch = new Branch(bankCode, branchCode);
        branches.put(branchCode, mainBranch); // χρησιμοποιούμε branchCode ως key
        
	}
	
	public Branch getBranch(String branchCode) {
        return branches.get(branchCode);
    }
		
	public void createUserCLI() {
		Scanner scanner = new Scanner(System.in);
		System.out.println("Select user type to create: Type 1 for Personal Customer, Type 2 for Business Customer");
		int choice = scanner.nextInt();
		scanner.nextLine(); // Consume newline
		switch (choice) {
		    case 1:
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
				break;
		    case 2:
		    	System.out.println("Type password: ");	//maybe should be hidden and have some rules
				String businessPassword = scanner.nextLine();
				System.out.println("Type business email: ");
				String businessEmail = scanner.nextLine();
				System.out.println("Type business name: ");
				String businessName = scanner.nextLine();
				System.out.println("Type representative name: ");
				String representativeName = scanner.nextLine();
				System.out.println("Type business phone number: ");
				String businessPhoneNumber = scanner.nextLine();
				
				String businessUserID = generateId(5); //5 for businessCustomer (different from simple customer)
				User newBusinessCustomer = new businessCustomer(businessUserID, businessPassword, businessEmail, businessName, representativeName, businessPhoneNumber);//create business customer
				customers.put(businessUserID, newBusinessCustomer);
		        break;
		    default:
		        System.out.println("Invalid choice. Please select 1 or 2.");
		        break;
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
	
	protected void transferMoney(Customer customer) {	//method is here because bankSystem can see all Accounts
		//select account to transfer from and account to transfer to
		Scanner scanner = new Scanner(System.in);
		System.out.println("Type the account number you want to transfer from: ");
		String fromAccountNumber = scanner.nextLine();
		System.out.println("Type the account number you want to transfer to: ");
		String toAccountNumber = scanner.nextLine();
		System.out.println("Type the amount you want to transfer: ");
		double amount = scanner.nextDouble();
		Account fromAccount = customer.findAccountByNumber(fromAccountNumber);
		Account toAccount = getAccountbyNumber(toAccountNumber);
		if (amount <= 0) {
			System.out.println("Amount must be positive.");
			scanner.close();
			return;
		}
		if (fromAccount.getIBAN()==null || toAccount.getIBAN()==null) {	
			System.out.println("One or both of the account numbers are invalid.");
			scanner.close();
			return;
		}	
		if (fromAccount.equals(toAccount)) {
			System.out.println("You cannot transfer money to the same account.");
			scanner.close();
			return;
		}
		if(!(fromAccount instanceof TransactionalAccount)) {	//γινεται ελεγχος αν ο λογαριασμος υποστηριζει συναλαγες
			System.out.println("The source account does not support transactions.");
			scanner.close();
			return;
		}
		if (fromAccount.getBalance() < amount) {
			System.out.println("Insufficient funds in the source account.");
			scanner.close();
			return;
		}
		if(toAccount instanceof FixedTermAccount) {	//γινεται ελεγχος αν ο λογαριασμος μπορει να δεχεται χρηματα
			System.out.println("The destination account does not support transactions.");
			scanner.close();
			return;
		}
		if (fromAccount.getBranch().getBankCode() != toAccount.getBranch().getBankCode()) { // σε transfer σε αλλη τραπεζα προμηθεια 1 euro σε παραληπτη
			if(fromAccount.getBalance() < amount + 1) {
				System.out.println("Insufficient funds in the source account to cover the transfer fee.");
				scanner.close();
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
		scanner.close();	
	}
	
	//na ftiaxtei methodos gia plhromes klp
	
	
}
