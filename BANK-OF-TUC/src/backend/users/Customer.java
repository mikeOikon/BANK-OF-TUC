package backend.users;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Stack;

import backend.BankSystem;
import backend.Branch;
import backend.accounts.Account;
import backend.accounts.AccountBuilder;
import backend.accounts.AccountFactory;

import backend.transactions.Transaction;
import behaviors.CustomerBehavior;
import types.AccountType;
import types.UserType;

public class Customer extends User {
	
	private ArrayList<Account> accounts; //ισως δεν χρειαζεται και εδω λιστα αφου υπαρχει στους users
	
	public Customer(String userID, String username, String password, String name, String surname, Branch branch,String AFM) {
		super(userID, username, password, name, surname, branch, AFM);
		this.accounts = new ArrayList<Account>();
		this.userBehavior= new CustomerBehavior();
	}
	
	//na doume ti prepei na einai protected
	@Override
	public ArrayList<Account> getAccounts() {
	    return accounts; // άδεια λίστα αν δεν έχει
	}
	
	@Override
	public Account getPrimaryAccount() {
		for(Account account : getAccounts()) {
			if(account.isPrimary()) 
			 	return account;
		}
		return null;
	}
	
	public void setPrimaryAccount(Account targetAccount) {
	    // 1. Βρίσκουμε τον τρέχοντα primary (αν υπάρχει) και τον απενεργοποιούμε
	    Account currentPrimary = getPrimaryAccount();
	    if (currentPrimary != null) {
	        currentPrimary.setPrimary(false);
	    }

	    // 2. Ορίζουμε τον νέο λογαριασμό ως primary
	    if (targetAccount != null) {
	        targetAccount.setPrimary(true);
	    }
	}
	
	public UserType getUserType() {
		return UserType.CUSTOMER;
	}

	

	@Override
	public Account createAccount(int choice) {
	    AccountType type;

	    switch (choice) {
	        case 1:
	            type = AccountType.TRANSACTIONAL;           	
	            break;
	        case 2:
	            type = AccountType.SAVINGS;
	            break;
	        case 3:
	            type = AccountType.FIXED;
	            break;
	        default:
	            System.out.println("Invalid choice. Please select a valid account type.");
	            return null;
	    }

	    // Χρήση AccountFactory για δημιουργία λογαριασμού
	    AccountBuilder AccountBuilder = new AccountBuilder()
	            .withUserID(this.userID)
	            .withBalance(0.0)
	            .withTransactions(new Stack<Transaction>())
	            .withInterest(0.0) // default interest rate
	            .withBranch(this.branch);
	    Account newAccount = AccountFactory.createAccount(type, AccountBuilder);
	    boolean firstAccount = accounts.isEmpty();
	    accounts.add(newAccount);
	    if(firstAccount) {
	    	newAccount.setPrimary(true);
	    }
	    return newAccount;
	    //viewAccountDetails(newAccount);
	}

	
	protected void askToCloseAccount() {
		//methodos pou kleinei ton logaraismo(prepei o loariasmos na exei 0 balance kai na min einai fixed term)
	}
	
	protected void accountBalance(Account account) {
		System.out.println("The balance for account " + account.getIBAN() + " is: " + account.getBalance());
	}

	protected void viewAccountDetails(Account acc) {
		if (acc != null) {
			System.out.println(acc.toString());
		} else {
		    System.out.println("Account not found.");
		}
	}
	
	public void viewAccountsDetails() {
		//isos na grafei apo pote einai melos o customer
		
		System.out.println("Username: " + this.username);
		System.out.println("Name: " + this.name + " " + this.surname);
		System.out.println("Email: " + this.email);
		System.out.println("Phone Number: " + this.phoneNumber);
		System.out.println("Accounts:");
		for (Account acc : accounts) {
			viewAccountDetails(acc);
		}	
	}
		
	//na doume ti prepei na ginetai edw
	protected void payBills() {
		
	}
	
	
	public void viewTransactionHistory() {	//θα πρεπει να βγαζει τις συνναλαγες από ολους τους λογαριασμους;
		List<Transaction> allTransactions = new ArrayList<>();

		for (Account acc : accounts) {
			Stack<Transaction> transactions = acc.getTransactions();
		    allTransactions.addAll(transactions); // πρόσθεσε όλες τις συναλλαγές του λογαριασμού
		}
		// Ταξινόμηση κατά χρόνο, πιο πρόσφατες πρώτες
	    Collections.sort(allTransactions, new Comparator<Transaction>() {
	        @Override
	        public int compare(Transaction t1, Transaction t2) {
	            return t2.getTimestamp().compareTo(t1.getTimestamp()); // φθίνουσα σειρά
	        }
	    });
		if (allTransactions.isEmpty()) {
	        System.out.println("No transactions found for customer " + this.username);
	        return;
	    }
		
		System.out.println("Transaction history :");
		for (Transaction t : allTransactions) {
		    System.out.println(t);
		}
		
	}

	public Account findAccountByNumber(String accountNumber) {	//find account by iBAN
	    for (Account acc : accounts) {
	        if (acc.getIBAN().equals(accountNumber)) {
	            return acc;
	        }
	    }
	    return null; // αν δεν βρεθεί
	}

	public Object getFullName() {
		return this.name + " " + this.surname;
	}
	
	@Override
	public Account getAccountByID(String accountID) {
		for (Account account : getAccounts()) {
			if (account.getIBAN().equals(accountID)) {
				return account;
			}
		}
		return null;
	}
}
