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
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

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
        setVisible(true);
    }

    private void buildTabs() {
        // 1. PROFILE (Πάντα πρώτο)
        tabs.addTab("Profile", new ProfileTab(user));

        // 2. CUSTOMER TABS (Με σύνδεση refresh)
        if (user instanceof Customer customer) {
            // Δημιουργία των tabs
            MyTransactionsTab transactionsTab = new MyTransactionsTab(customer);
            CustomerOverviewTab overviewTab = new CustomerOverviewTab(customer);
            TransferTab transferTab = new TransferTab(customer, overviewTab);
            MyAccountsTab accountsTab = new MyAccountsTab(customer, overviewTab);

            //Συνδέουμε το Overview με τα υπόλοιπα tabs
            overviewTab.setOtherTabs(accountsTab, transactionsTab, transferTab);

            if (user.canViewAccounts()) {
                tabs.addTab("Overview", overviewTab);
                tabs.addTab("My Accounts", accountsTab);
            }
            if (user.canViewTransactionsHistory()) {
                tabs.addTab("My Transactions", transactionsTab);
            }
            if (user.canTransferMoney()) {
                tabs.addTab("Transfer", transferTab);
            }
        }

        // 3. ADMIN / AUDITOR TABS
        if (user.canViewAllAccounts()) {
            tabs.addTab("All Accounts", new AllAccountsTab(user));
        }
        if (user.canViewAllTransactionsHistory()) {
            tabs.addTab("All Transactions", new AllTransactionsTab());
        }
        if (user.canPromoteUser() || user.canDemoteUser() || user.canRemoveUsers()) {
            tabs.addTab("User Management", new UserManagementTab(user));
        }

        tabs.addTab("Settings", new SettingsTab());
    }
}