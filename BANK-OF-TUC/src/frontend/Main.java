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
	
	backend.accounts.Branch central = new backend.accounts.Branch("001", "0001");
	backend.accounts.Branch athens  = new backend.accounts.Branch("001", "0002");
	
	BankSystem centralBank = new BankSystem();
	
	System.out.println("=== Bank system quick test ===");


    System.out.println("Branch CENTRAL: bank=" + central.getBankCode() + " branch=" + central.getBranchCode());
    System.out.println("Branch ATHENS : bank=" + athens.getBankCode() + " branch=" + athens.getBranchCode());
    System.out.println();

    // --- Δημιουργία πελατών ---
   // Customer alice = new Customer("alice01", "pass", "alice@mail", "Alice", "Pap", "6970000001", central);
   // Customer bob   = new Customer("bob02",   "pass", "bob@mail",   "Bob",   "Kok", "6970000002", athens);

   // System.out.println("Created customers: " + alice.getUserID() + ", " + bob.getUserID());
   // System.out.println();
    

    // --- Δημιουργία λογαριασμών για τους πελάτες ---
    // ΣΗΜΕΙΩΣΗ: Οι παρακάτω constructors υποθέτουν ότι οι υποκλάσεις έχουν:
    // public TransactionalAccount(String userID, double balance, ArrayList<String> transactions, Branch branch) { super(...); }
    // Το ίδιο για SavingsAccount και FixedTermAccount.
    //
    // Αν οι υποκλάσεις σου δεν έχουν τέτοιους constructors, πρόσθεσέ τους με την κλήση προς super(...) όπως παραπάνω.

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
        
    //    if (t1 instanceof TransactionalAccount) 
	//		((TransactionalAccount) t1).addMoney(150.0);
    //    System.out.println(" Alice " + t1.getClass() + " New Balance: " + t1.getBalance());
        
      //  if(user1.getAccounts().get(0) instanceof TransactionalAccount) {
	//		((TransactionalAccount) user1.getAccounts().get(0)).addMoney(300.0);
     //   }
     //   centralBank.transferMoney(alice);
       
       
        
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
