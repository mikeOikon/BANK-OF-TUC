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
import backend.accounts.Account;
import backend.accounts.Branch;
import backend.accounts.FixedTermAccount;
import backend.accounts.PersonalAccount;
import backend.accounts.SavingsAccount;
import backend.accounts.Transaction;
import backend.accounts.TransactionalAccount;

public class Customer extends User {
	private Branch branch;
	private ArrayList<Account> accounts; //ισως δεν χρειαζεται και εδω λιστα αφου υπαρχει στους users
	
	public Customer(String userID, String username, String password, String email, String name, String surname, String phoneNumber, Branch branch) {
		super(userID, username, password, email, name, surname, phoneNumber, branch);
		this.accounts = new ArrayList<Account>();
	}
	
	//na doume ti prepei na einai protected
	
	public ArrayList<Account> getAccounts() {
		if (accounts.isEmpty()) {
			System.out.println("No accounts found for customer " + this.userID);
			return null;
		} else {
			return accounts;
		}
	}

	
	public void createAccountMenu() {
		System.out.println("What type of account do you want to create? Type 1 for Transactional, Type 2 for Savings, Type 3 for Fixed-Term");
		int choice = frontend.Main.scanner.nextInt();
		frontend.Main.scanner.nextLine(); // Consume newline
		createAccount(choice);
	}
	
	protected void createAccount(int choice) {
		switch (choice) {
		    case 1:
		    	//create transactional account
		    	Account newTransactionalAccount = new TransactionalAccount(this.userID, 0.0, new Stack<Transaction>(), Branch.getDefaultBranch());
		    	accounts.add(newTransactionalAccount);
		    	viewAccountDetails(newTransactionalAccount);
		    	break;
		    case 2:
		    	//create savings account
		    	Account newSavingsAccount = new SavingsAccount(this.userID, 0.0, new Stack<Transaction>(), Branch.getDefaultBranch());
		    	accounts.add(newSavingsAccount);
		    	viewAccountDetails(newSavingsAccount);
		    	break;
		    case 3:
		    	//create fixed-term account
		    	Account newFixedTermAccount = new FixedTermAccount(this.userID, 0.0, new Stack<Transaction>(), Branch.getDefaultBranch());
		    	accounts.add(newFixedTermAccount);
		    	viewAccountDetails(newFixedTermAccount);
		    	break;
		    default:
		        System.out.println("Invalid choice. Please select a valid account type.");
		        break;
		}
	}
	
	protected void askToCloseAccount() {
		//methodos pou kleinei ton logaraismo(prepei o loariasmos na exei 0 balance kai na min einai fixed term)
	}
	
	protected void accountBalance(Account account) {
		System.out.println("The balance for account " + account.getIBAN() + " is: " + account.getBalance());
	}

	protected void viewAccountBalance() {
		System.out.println("Type the acount iBAN you want to check the balance");
		String accountNumber = frontend.Main.scanner.nextLine();	
		Account account = findAccountByNumber(accountNumber);
		if (account != null) {
		    accountBalance(account);
		} else {
		    System.out.println("Account with iBAN " + accountNumber + " not found.");
		}	
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
		
		System.out.println("Customer ID: " + this.userID);
		System.out.println("Name: " + this.name + " " + this.surname);
		System.out.println("Email: " + this.email);
		System.out.println("Phone Number: " + this.phoneNumber);
		System.out.println("Accounts:");
		for (Account acc : accounts) {
			viewAccountDetails(acc);
		}	
	}
		
	
	protected void payBills() {
		
	}
	
	
	protected void viewTransactionHistory() {	//θα πρεπει να βγαζει τις συνναλαγες από ολους τους λογαριασμους;
		//για καθε λογαριασμο θα πρεπει να καλειται η getTransactionHistory και να εμφανιζει τις συναλαγες με χρονική σειρά από τις πιο πρόσφατες στις πιο παλιές
		//format: ωρα, λογαριασμος, ποσό, λογαριασαμός προορισμού (αν υπάρχει), τύπος συναλλαγής
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
	        System.out.println("No transactions found for customer " + this.userID);
	        return;
	    }
		
		System.out.println("Transaction history :");
		for (Transaction t : allTransactions) {
		    System.out.println(t);
		}
		
		//αν θελουμε να ειναι με βαση τον χρονο μονο (δηλαδη ολοι οι λογαριασμοι να εμφανιζονται και να λεει ποιος λογαριασμος εγινε η συναλαγη) θα πρεπει να φτιαξουμε μια stack με ολες τις συναλαγες σε ολους τους λογαριασμους στον user και να μην καλουμε ξεχωριστες stack για καθε λογαριασμο 
	}
	
	protected void updatePersonalInformation() {
		//if password is correct then let user update email, name, surname, phone number, and maybe password
		System.out.println("To update your personal information, please enter your current password:");
		String currentPassword = frontend.Main.scanner.nextLine();
		if (currentPassword.equals(this.password)) {
		    System.out.println("Enter new email (or press Enter to keep current):");
		    String newEmail = frontend.Main.scanner.nextLine();
		    if (!newEmail.isEmpty()) {
		        this.email = newEmail;
		    }

		    System.out.println("Enter new name (or press Enter to keep current):");
		    String newName = frontend.Main.scanner.nextLine();
		    if (!newName.isEmpty()) {
		        this.name = newName;
		    }

		    System.out.println("Enter new surname (or press Enter to keep current):");
		    String newSurname = frontend.Main.scanner.nextLine();
		    if (!newSurname.isEmpty()) {
		        this.surname = newSurname;
		    }

		    System.out.println("Enter new phone number (or press Enter to keep current):");
		    String newPhoneNumber = frontend.Main.scanner.nextLine();
		    if (!newPhoneNumber.isEmpty()) {
		        this.phoneNumber = newPhoneNumber;
		    }

		    System.out.println("Do you want to change your password? (yes/no)");
		    String changePassword = frontend.Main.scanner.nextLine();
		    if (changePassword.equalsIgnoreCase("yes")) {
		        System.out.println("Enter new password:");
		        String newPassword = frontend.Main.scanner.nextLine();
		        this.password = newPassword;
		    }

		    System.out.println("Personal information updated successfully.");
		} else {
		    System.out.println("Incorrect password. Personal information not updated.");
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
}
