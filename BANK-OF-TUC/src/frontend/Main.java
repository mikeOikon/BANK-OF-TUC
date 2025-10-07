package frontend;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.Stack;

//import javax.swing.*;	//GUI components

import backend.accounts.Branch;
import backend.BankSystem;
import backend.accounts.Account;
import backend.accounts.TransactionalAccount;
import backend.accounts.SavingsAccount;
import backend.accounts.FixedTermAccount;
import backend.users.Customer;
import backend.users.User;
import backend.accounts.Transaction;

public class Main {
	
	public static final Scanner scanner = new Scanner(System.in);	//για να διαβάζουμε από το πληκτρολόγιο
	
	// Μια γρήγορη δοκιμή για να δεις αν μπορείς να δημιουργήσεις αντικείμενα
	
	public static void main(String[] args) {
	// --- Δημιουργία υποκαταστημάτων ---

	BankSystem centralBank = new BankSystem();
	
	System.out.println("=== Bank system quick test ===");
	
    try {
    	//create users
    	User user1 = centralBank.createUserCLI();
    	User user2 = centralBank.createUserCLI();
    	
    	//if users are customers create accounts for them
    	if(user1 instanceof Customer) 
			((Customer) user1).createAccountMenu();
        
    	if(user1 instanceof Customer) 
			((Customer) user1).createAccountMenu();
    	
        if(user2 instanceof Customer)
        	((Customer) user2).createAccountMenu();
        

        
       System.out.println("++++++++++");
       
        centralBank.getAllCustomers();      	
        	
        if(user1 instanceof Customer) {
        	Account t1 = ((Customer) user1).getAccounts().get(0);
        	((TransactionalAccount) t1).addMoney(150.0);
			centralBank.transferMoney((Customer)user1);
        }
        
        System.out.println(((Customer)user1).getAccounts());
        System.out.println(((Customer)user2).getAccounts());
        
        if(user2 instanceof Customer) {
			centralBank.transferMoney((Customer)user2);
        }
        
        //η login θελει φτιαξιμο
       
        
        scanner.close();	
        
    } catch (NoSuchMethodError | Exception e) {
        // Αν δεν υπάρχουν οι κατάλληλοι constructors ή εμφανιστούν runtime σφάλματα,
        // εμφανίζουμε ενημέρωση για το τι χρειάζεται να διορθωθεί.
        System.err.println("Could not construct Subclass accounts directly. Exception:");
        e.printStackTrace();
        System.err.println();
        System.err.println("Βεβαιώσου ότι οι υποκλάσεις έχουν constructors όπως:");
        System.err.println("public TransactionalAccount(String userID, double balance, ArrayList<String> transactions, Branch branch) {");
        System.err.println("    super(userID, balance, transactions, branch);");
        System.err.println("}");
    }

    System.out.println();
    System.out.println("=== End of quick test ===");

    // --- Προαιρετικό: αν έχεις υλοποιήσει createAccount(Branch) στην Customer ---
    // τότε μπορείς να καλέσεις:
    //
    // alice.createAccount(central);
    //
    // ή αν η μέθοδός σου είναι interactive (Scanner), τρέξε την και ακολούθησε τις οδηγίες.
    
    
}
	
}
