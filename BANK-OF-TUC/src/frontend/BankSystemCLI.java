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
    
    public static void main(String[] args) {
        BankSystemCLI cli = new BankSystemCLI();
        cli.start();
    }

    public void start() {
    	
    	/*System.out.println("                                                           5                                                                  \r\n"
    			+ "                                                         5 47                                                                 \r\n"
    			+ "                                                        15 41                                                                 \r\n"
    			+ "                                                       375122                                                                 \r\n"
    			+ "                                                      5132574                                                                 \r\n"
    			+ "                                                      515 4723                                                                \r\n"
    			+ "                                                      33331274                                                                \r\n"
    			+ "                                                       515 2134             7 7 7                                             \r\n"
    			+ "                                                       121475755             211                                              \r\n"
    			+ "                                                      21231215 42           71 7                                              \r\n"
    			+ "                                                       47521214 53                                                            \r\n"
    			+ "                                                       75 521214355                                                           \r\n"
    			+ "                                                        741444444445                                                          \r\n"
    			+ "                                                         2441  744441                                                         \r\n"
    			+ "                                                         57      7442                                                         \r\n"
    			+ "                                                         371     244                                                          \r\n"
    			+ "                                                     151       72442                                                          \r\n"
    			+ "                                                   1       2  254441                                                          \r\n"
    			+ "                                                  7       3444444445           2                                              \r\n"
    			+ "                                                 1    1    4444               723                                             \r\n"
    			+ "                                                2      1   744445533441       533                                             \r\n"
    			+ "                                                       32           44457    15111                                            \r\n"
    			+ "                                               3      44447  125217   7352   47213                                            \r\n"
    			+ "                                               1         253                57332                                             \r\n"
    			+ "                                               3        7         13231    272311                                             \r\n"
    			+ "                                                127   3       3 3444444445572113                                              \r\n"
    			+ "                                                     5      7    3444444444412271                                             \r\n"
    			+ "                                               344444       37    24444444444313                                              \r\n"
    			+ "                                              7544444      1  3    544444444447                                               \r\n"
    			+ "                                              3444444      7 344    4444444442                                                \r\n"
    			+ "                                              24444443      244447   444444442                                                \r\n"
    			+ "                                              444444447      244442   4444444                                                 \r\n"
    			+ "                                             34444444441       24442    2445                                                  \r\n"
    			+ "                                                                                                                              \r\n"
    			+ "                                                                                                                              \r\n"
    			+ "                                                                                                                              \r\n"
    			+ "                                              41                                                                              \r\n"
    			+ "                                            244453 34    54  144255                                                           \r\n"
    			+ "                                              47   34    54 14                                                                \r\n"
    			+ "                                              41   34    55 14                                                                \r\n"
    			+ "                                              243   542245   342125                                                           \r\n"
    			+ "                                              21                          27                                                  \r\n"
    			+ "                                              51                          51                                                  \r\n"
    			+ "                                              5552255   14525245 5552242  51  42                                              \r\n"
    			+ "                                              52    34 14     45 52    53 5224                                                \r\n"
    			+ "                                              52    34 14     55 53    53 53343                                               \r\n"
    			+ "                                              5552245   14522245 53    53 51  55           ");*/
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
