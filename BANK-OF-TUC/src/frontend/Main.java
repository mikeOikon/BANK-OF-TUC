package frontend;

import java.util.Scanner;
import backend.BankSystem;
import backend.users.Customer;
import backend.users.User;
import backend.accounts.Account;
import backend.transactions.Transaction;
import backend.transactions.TransactionFactory;
import backend.transactions.TransactionType;

//MONO AEK 21
public class Main {
	
    public static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

        // --- Load BankSystem from file or create new ---
        BankSystem centralBank = BankSystem.getInstance();
        System.out.println("=== Bank of TUC System ===");

       // TransactionInvoker invoker = new TransactionInvoker();

        try {
            // --- Create new customers ---
            User user1 = centralBank.createUserCLI();
            User user2 = centralBank.createUserCLI();

            // Print all customers
            centralBank.getAllCustomers();

            // --- Let customers create accounts ---
            if (user1 instanceof Customer)
                ((Customer) user1).createAccountMenu();

            if (user2 instanceof Customer)
                ((Customer) user2).createAccountMenu();

            // --- Perform deposit/withdraw/transfer using Command Pattern ---
            if (user1 instanceof Customer customer1) {
                Account acc1 = customer1.getAccounts().get(0); // first account
                // Deposit 100
                Transaction deposit = TransactionFactory.createTransaction(TransactionType.DEPOSIT, acc1, 100);
                //TransactionCommand depositCmd = new TransactionCommand(deposit);
                //invoker.executeCommand(depositCmd);

                // Withdraw 50
                Transaction withdraw = TransactionFactory.createTransaction(TransactionType.WITHDRAW, acc1, 50);
                //TransactionCommand withdrawCmd = new TransactionCommand(withdraw);
                //invoker.executeCommand(withdrawCmd);

                // Transfer to user2 if possible
                if (user2 instanceof Customer customer2 && !customer2.getAccounts().isEmpty()) {
                    Account acc2 = customer2.getAccounts().get(0);
                    Transaction transfer = TransactionFactory.createTransaction(acc1, acc2, 30);
                    //TransactionCommand transferCmd = new TransactionCommand(transfer);
                    //invoker.executeCommand(transferCmd);
                }
            }

        } catch (Exception e) {
            System.err.println("Error during demo: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // --- Save BankSystem to file ---
            centralBank.shutdown();
            scanner.close();
            System.out.println("=== End of session ===");
        }
    }
}
