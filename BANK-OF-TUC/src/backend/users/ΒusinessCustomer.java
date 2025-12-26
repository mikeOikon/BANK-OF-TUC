package backend.users;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;

import backend.Branch;
import backend.accounts.Account;
import backend.accounts.AccountBuilder;
import backend.accounts.AccountFactory;
import backend.accounts.BusinessAccount;
import backend.transactions.Transaction;
import behaviors.BusinessBehavior;
import types.AccountType;
import types.UserType;

public class ΒusinessCustomer extends User{
	
	private ArrayList<Account> accounts;
	
	public ΒusinessCustomer(String userID,String username, String password, Branch branch, String businessName, String representativeName, String AFM) {
		super(userID, username, password, businessName, representativeName, branch, AFM);
		this.accounts = new ArrayList<>();
		this.userBehavior= new BusinessBehavior();
	}
	//na doume ti prepei na einai protected 
	
	
	@Override
	public ArrayList<Account> getAccounts() {
		if (this.accounts.isEmpty()) {
			//System.out.println("No accounts found for customer " + this.userID);
			return null;
		} else {
			return this.accounts;
		}
	}
	
	@Override
	public Account getPrimaryAccount() {
		for(Account account : getAccounts()) {
			if(account.isPrimary()) 
			 	return account;
		}
		return null;
	}
	
	public UserType getUserType() {
		return UserType.BUSINESSCUSTOMER;
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
	
	
	@Override
	public Account createAccount(int choice) {
	    AccountType type;

	    type = AccountType.BUSINESS ;           	

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
		//
	}
	

	/*protected void viewAccountBalance() {	//ιδια ακριβως με customer
		Scanner scanner = new Scanner(System.in);
		System.out.println("Type the acount iBAN you want to check the balance");
		String accountNumber = scanner.nextLine();	
		Account account = findAccountByNumber(accountNumber);
		if (account != null) {
		    accountBalance(account);
		} else {
		    System.out.println("Account with iBAN " + accountNumber + " not found.");
		}
		scanner.close();	//ισως δεν πρεπει να κλεισει εδω		
	}
	
	protected void viewAccountDetails(Account acc) { //mporoyme na toy valoume na emfanizei kai se poio branch anhkei
		if (acc != null) {
			System.out.println("Account iBAN: " + acc.getIBAN() + ", Balance: " + acc.getBalance());
		} else {
		    System.out.println("Account not found.");
		}
	}*/
	
	/*protected void viewAccountsDetails() {
		//na grafei apo pote einai melos o customer
		
		System.out.println("Username: " + this.username);
		System.out.println("Business Name: " + this.name + ", Representative Name " + this.surname);
		System.out.println("Email: " + this.email);
		System.out.println("Phone Number: " + this.phoneNumber);
		System.out.println("Accounts:");
		for (Account acc : accounts) {
			viewAccountDetails(acc);
		}	

	}*/
	
	protected void transferMoney() {
		//να πηγαινει μηνυμα στην τράπεζα για να γίνει η μεταφορά 
		//καπως να γινεται ελεγχοσ ποσα λεφτα μεταφερονται γενικα
		
	}
	
	//να doume ti prepei na ginetai edw
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
	        System.out.println("No transactions found for customer " + this.username);
	        return;
	    }
		
		System.out.println("Transaction history :");
		for (Transaction t : allTransactions) {
		    System.out.println(t);
		}
		
		//αν θελουμε να ειναι με βαση τον χρονο μονο (δηλαδη ολοι οι λογαριασμοι να εμφανιζονται και να λεει ποιος λογαριασμος εγινε η συναλαγη) θα πρεπει να φτιαξουμε μια stack με ολες τις συναλαγες σε ολους τους λογαριασμους στον user και να μην καλουμε ξεχωριστες stack για καθε λογαριασμο 
	}
	
	/*protected void updatePersonalInformation() {
		System.out.println("To update your business information, please enter your current password:");
		String currentPassword = frontend.Main.scanner.nextLine();
		if (currentPassword.equals(this.password)) {
			viewBusinessInformation(); // Display current information
			System.out.println("Type new username (or press Enter to keep current): ");
			String newUsername = frontend.Main.scanner.nextLine();
			if (!newUsername.isEmpty()) {
				setUsername(newUsername);
			}
			System.out.println("Type new email (or press Enter to keep current): ");
			String newEmail = frontend.Main.scanner.nextLine();
			if (!newEmail.isEmpty()) {
				setEmail(newEmail);
			}
			System.out.println("Type new business name (or press Enter to keep current): ");
			String newBusinessName = frontend.Main.scanner.nextLine();
			if (!newBusinessName.isEmpty()) {
				setName(newBusinessName);
			}
		
			System.out.println("Type new representative name (or press Enter to keep current): ");
			String newRepName = frontend.Main.scanner.nextLine();
			if (!newRepName.isEmpty()) {
				setSurname(newRepName);
			}
		
			System.out.println("Type new phone number (or press Enter to keep current): ");
			String newPhoneNumber = frontend.Main.scanner.nextLine();
			if (!newPhoneNumber.isEmpty()) {
				setPhoneNumber(newPhoneNumber);
			}
		} else {
			System.out.println("Incorrect password. Personal information not updated.");
			return;
		}
		System.out.println("Personal information updated.");
		viewBusinessInformation();	//show updated information
	}
*/
	public Account findAccountByNumber(String accountNumber) {	//find account by iBAN (ιδιο με customer)
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
	
	/*protected void accountBalance(Account account) { //ιδιο ακριβως με customer
		System.out.println("The balance for account " + account.getIBAN() + " is: " + account.getBalance());
	}*/
	
	/*private void viewBusinessInformation() {
		System.out.println("Business Name: " + this.name);
		System.out.println("Representative Name: " + this.surname);
		System.out.println("Email: " + this.email);
		System.out.println("Phone Number: " + this.phoneNumber);
	}*/
}
