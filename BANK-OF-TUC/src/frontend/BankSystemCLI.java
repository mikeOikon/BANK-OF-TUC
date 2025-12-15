package frontend;

import backend.BankSystem;
import backend.accounts.Account;
import backend.users.User;
import services.UserManager;

import java.util.List;
import java.util.Scanner;

public class BankSystemCLI {

    private final BankSystem bankSystem;
    private final Scanner scanner;

    public BankSystemCLI() {
        this.bankSystem = BankSystem.getInstance();
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        System.out.println("=== Welcome to the Bank System ===");

        User currentUser = null;
        while (currentUser == null) {
            System.out.println("1. Log in");
            System.out.println("2. Create new account");
            System.out.print("Select option: ");
            String input = scanner.nextLine();

            switch (input) {
                case "1":
                    currentUser = bankSystem.loginCLI();
                    break;
                case "2":
                    currentUser = bankSystem.createUserCLI();
                    if(currentUser != null) System.out.println("✅ Account created successfully!");
                    break;
                default:
                    System.out.println("Invalid option. Try again.");
            }
        }

        // Main menu loop
        while (true) {
            System.out.println("\n=== Main Menu ===");
            int optionNumber = 1;

            int transferOption = -1, viewAccountsOption = -1, viewTransactionsOption = -1;
            int removeUserOption = -1, viewAllAccountsOption = -1, viewAllTransactionsOption = -1;
            int createAuditorOption = -1, promoteUserOption = -1, demoteUserOption = -1, assistUserOption = -1;

            if (currentUser.canViewAccounts()) {
                viewAccountsOption = optionNumber;
                System.out.println(optionNumber++ + ". View my accounts");
            }
            if (currentUser.canTransferMoney()) {
                transferOption = optionNumber;
                System.out.println(optionNumber++ + ". Transfer money");
            }
            if (currentUser.canViewTransactionsHistory()) {
                viewTransactionsOption = optionNumber;
                System.out.println(optionNumber++ + ". View transaction history");
            }
            if (currentUser.canRemoveUsers()) {
                removeUserOption = optionNumber;
                System.out.println(optionNumber++ + ". Remove a user");
            }
            if (currentUser.canViewAllAccounts()) {
                viewAllAccountsOption = optionNumber;
                System.out.println(optionNumber++ + ". View all accounts");
            }
            if (currentUser.canViewAllTransactionsHistory()) {
                viewAllTransactionsOption = optionNumber;
                System.out.println(optionNumber++ + ". View all transactions");
            }
            if (currentUser.canCreateAuditor()) {
                createAuditorOption = optionNumber;
                System.out.println(optionNumber++ + ". Create auditor");
            }
            if (currentUser.canPromoteUser()) {
                promoteUserOption = optionNumber;
                System.out.println(optionNumber++ + ". Promote user");
            }
            if (currentUser.canDemoteUser()) {
                demoteUserOption = optionNumber;
                System.out.println(optionNumber++ + ". Demote user");
            }
            if (currentUser.canAssistUsers()) {
                assistUserOption = optionNumber;
                System.out.println(optionNumber++ + ". Assist a customer");
            }

            System.out.println(optionNumber + ". Log out");
            int logoutOption = optionNumber;

            System.out.print("Select option: ");
            String choice = scanner.nextLine();

            try {
                int selected = Integer.parseInt(choice);

                if (selected == logoutOption) {
                    System.out.println("Logging out...");
                    break;
                } else if (selected == viewAccountsOption) {
                    /*for (Account acc : currentUser.getAccounts()) {
                        System.out.println(acc);
                    }*/
                    
                } else if (selected == transferOption) {
                    //bankSystem.transferMoney(currentUser);
                }
                else if (selected == viewTransactionsOption) {
                    /*currentUser.getAccounts().forEach(acc -> {
                        acc.getTransactions().forEach(System.out::println);
                    });*/
                } else if (selected == removeUserOption) {
                    System.out.print("Enter user ID to remove: ");
                    String id = scanner.nextLine();
                    bankSystem.removeUser(id);
                    
                    
                } else if (selected == viewAllAccountsOption) {
                    List<Account> accounts = bankSystem.getAllAccounts();
                    accounts.forEach(System.out::println);
                    
                } else if (selected == viewAllTransactionsOption) {
                    System.out.print("Enter customer ID: ");
                    String customerId = scanner.nextLine();
                    bankSystem.viewTransactionsByCustomer(customerId);
                    
                } else if (selected == createAuditorOption) {
                    System.out.println("Feature not implemented yet.");
                } else if (selected == promoteUserOption) {
                    System.out.println("Feature not implemented yet.");
                } else if (selected == demoteUserOption) {
                    System.out.println("Feature not implemented yet.");
                } else if (selected == assistUserOption) {
                    System.out.println("Feature not implemented yet.");
                } else {
                    System.out.println("Invalid choice, try again.");
                }

            } catch (NumberFormatException e) {
                System.out.println("Invalid input, type a number.");
            }
        }

        start(); // Restart CLI after logout
    }

}
