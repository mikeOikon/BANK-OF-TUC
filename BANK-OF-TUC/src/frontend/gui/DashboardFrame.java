package frontend.gui;

import backend.BankSystem;
import backend.users.Customer;
import backend.users.User;
import frontend.gui.tabs.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class DashboardFrame extends JFrame {

    private final User user;
    private final JTabbedPane tabs;

    public DashboardFrame() {
        user = UserSession.getInstance().getCurrentUser();

        if (user == null) {
            throw new IllegalStateException("No logged-in user");
        }

        setTitle("Bank of TUC — Dashboard");
        setSize(900, 600);
        setLocationRelativeTo(null);

        // Αλλάζουμε το close operation για να χειριστούμε shutdown
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        // Listener για να σώσουμε δεδομένα πριν κλείσει η εφαρμογή
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                BankSystem.getInstance().shutdown();
                System.exit(0);
            }
        });

        tabs = new JTabbedPane(JTabbedPane.TOP);
        add(tabs);

        buildTabs();

        // Ορατό το JFrame στο τέλος
        setVisible(true);
    }


    private void buildTabs() {

        // PROFILE (ALL USERS)
        tabs.addTab("Profile", new ProfileTab(user));

        // CUSTOMER START PAGE
        if (user instanceof Customer) {
        	Customer customer = (Customer) user;
        	CustomerOverviewTab overview = new CustomerOverviewTab(customer);
            tabs.addTab("Overview", overview);
            tabs.addTab("My Accounts", new MyAccountsTab(customer, overview));
        
            tabs.addTab("My Transactions", new MyTransactionsTab(customer));
        

        if (user.canTransferMoney()) {
            tabs.addTab("Transfer", new TransferTab(customer, overview));
        }
        }
        // ADMIN / AUDITOR
        if (user.canViewAllAccounts()) {
            tabs.addTab("All Accounts", new AllAccountsTab());
        }

        if (user.canViewAllTransactionsHistory()) {
            tabs.addTab("All Transactions", new AllTransactionsTab());
        }

        if (user.canPromoteUser() || user.canDemoteUser() || user.canRemoveUsers()) {
            tabs.addTab("User Management", new UserManagementTab());
        }

        // SETTINGS (ALL USERS)
        tabs.addTab("Settings", new SettingsTab());
        
        
    }
    
    

}
