package backend.users;

import java.util.List;

import backend.BankSystem;
import backend.Branch;
import backend.accounts.Account;
import behaviors.AuditorBehavior;
import types.UserType;

public class Auditor extends User {
	

	public Auditor(String userID, String username, String password, String name, String surname, Branch branch,String AFM) {
		super(userID,username, password, name, surname, branch,AFM);
		this.userBehavior = new AuditorBehavior();
		
	}
	
	public UserType getUserType() {
		return UserType.AUDITOR;
	}

	//Προβολή όλων των πελατών
    /*public void viewAllCustomers(BankSystem bank) {
        System.out.println("=== All Customers ===");
        bank.getAllCustomers();
    }*/

    //Προβολή όλων των λογαριασμών στη βάση
    /*public void viewAllAccounts(BankSystem bank) {
        System.out.println("=== All Accounts in the System ===");
        List<Account> allAccounts = bank.getAllAccounts();
        for (Account acc : allAccounts) {
            System.out.println(acc.toString());	//get information of all accounts
        }
    }*/

    //Προβολή ιστορικού συναλλαγών συγκεκριμένου πελάτη
    /*public void viewTransactionsOfCustomer(BankSystem bank, String customerID) {
        System.out.println("=== Transaction history for customer " + customerID + " ===");
        bank.viewTransactionsByCustomer(customerID);
    }*/

    /*
    //Προβολή συνολικού balance όλων των πελατών
    public void viewTotalBankBalance(BankSystem bank) {
        double total = bank.getTotalBalance();
        System.out.println("Total funds across all accounts: " + total);
    }
	*/
    
    //Αναζήτηση λογαριασμού με IBAN
    public void findAccountByIBAN(BankSystem bank, String iban) {
        Account acc = bank.getAccountbyNumber(iban);
        if (acc != null) {
            System.out.println(acc.toString());
        } else {
            System.out.println("No account found with IBAN: " + iban);
        }
    }
    
}
