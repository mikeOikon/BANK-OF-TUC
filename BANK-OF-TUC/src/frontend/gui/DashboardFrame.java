package frontend.gui;

import backend.users.User;
import frontend.gui.tabs.AllAccountsTab;
import frontend.gui.tabs.AllTransactionsTab;
import frontend.gui.tabs.CustomerOverviewTab;
import frontend.gui.tabs.MyAccountsTab;
import frontend.gui.tabs.MyTransactionsTab;
import frontend.gui.tabs.TransferTab;
import frontend.gui.tabs.UserManagementTab;

import javax.swing.*;
import java.awt.*;

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
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        tabs = new JTabbedPane(JTabbedPane.TOP);
        add(tabs);

        buildTabs();
    }

    private void buildTabs() {

        // CUSTOMER START PAGE
        if (user.canViewAccounts()) {
            tabs.addTab("Overview", new CustomerOverviewTab());
        }

        if (user.canViewAccounts()) {
            tabs.addTab("My Accounts", new MyAccountsTab());
        }

        if (user.canViewTransactionsHistory()) {
            tabs.addTab("My Transactions", new MyTransactionsTab());
        }

        if (user.canTransferMoney()) {
            tabs.addTab("Transfer", new TransferTab());
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
    }
}
