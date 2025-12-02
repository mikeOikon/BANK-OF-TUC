package frontend;

import java.util.Scanner;
import backend.BankSystem;
import backend.users.Customer;
import backend.users.User;

public class FileDemo {

    public static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

        BankSystem centralBank = BankSystem.getInstance();
        System.out.println("=== Bank of TUC System ===");

        try {
            // Create new customers
            User user1 = centralBank.createUserCLI();
            User user2 = centralBank.createUserCLI();

           /* // Add customers to system if needed
            if (user1 instanceof Customer c1)
                centralBank.addCustomer(c1);
            if (user2 instanceof Customer c2)
                centralBank.addCustomer(c2);*/

            // Print all customers
            centralBank.getAllCustomers();

        } catch (Exception e) {
            System.err.println("Error during demo: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Save before exit
            centralBank.shutdown();
            scanner.close();
            System.out.println("=== End of session ===");
        }
    }
}
