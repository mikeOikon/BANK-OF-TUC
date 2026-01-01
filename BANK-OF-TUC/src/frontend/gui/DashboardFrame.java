package frontend.gui;

import backend.BankSystem;
import backend.users.*;
import frontend.gui.tabs.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

public class DashboardFrame extends JFrame {

    private final User user;
    private final JTabbedPane tabbedPane;
    private final List<Refreshable> refreshableTabs = new ArrayList<>();

    public DashboardFrame() {
        user = UserSession.getInstance().getCurrentUser();

        if (user == null) {
            throw new IllegalStateException("No logged-in user");
        }

        setTitle("Bank of TUC — Dashboard");
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setLayout(new BorderLayout());

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                BankSystem.getInstance().shutdown();
                System.exit(0);
            }
        });

        tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        add(tabbedPane, BorderLayout.CENTER);

        buildTabs();
        setVisible(true);
    }

    /**
     * Δημιουργία tabs & σύνδεση refresh
     */
    private void buildTabs() {

        // --- PROFILE (πάντα πρώτο) ---
        tabbedPane.addTab("Profile", new ProfileTab(user));

        // --- CUSTOMER / BUSINESS CUSTOMER ---
        if (user instanceof Customer || user instanceof BusinessCustomer) {

            CustomerOverviewTab overviewTab = new CustomerOverviewTab(user);
            MyAccountsTab accountsTab = new MyAccountsTab(user, overviewTab);
            MyTransactionsTab transactionsTab = new MyTransactionsTab(user);
            TransferTab transferTab = new TransferTab(user, overviewTab);
            BillPaymentTab billPaymentTab = new BillPaymentTab(user, this);

            overviewTab.setOtherTabs(accountsTab, transactionsTab, transferTab);

            if (user.canViewAccounts()) {
                tabbedPane.addTab("Overview", overviewTab);
                tabbedPane.addTab("My Accounts", accountsTab);
                refreshableTabs.add(overviewTab);
                refreshableTabs.add(accountsTab);
            }

            if (user.canViewTransactionsHistory()) {
                tabbedPane.addTab("My Transactions", transactionsTab);
                refreshableTabs.add(transactionsTab);
            }

            if (user.canTransferMoney()) {
                tabbedPane.addTab("Transfer", transferTab);
                refreshableTabs.add(transferTab);
            }

            if (user.canPayBills()) {
                tabbedPane.addTab("Pay Bills", billPaymentTab);
                refreshableTabs.add(billPaymentTab);
            }

            if (user.canIssueBills()) {
                IssueBillTab issueBillTab = new IssueBillTab(user);
                tabbedPane.addTab("Issue Bills", issueBillTab);
                refreshableTabs.add(issueBillTab);
            }

            if (user.canOpenTicket()) {
                SupportTab supportTab = new SupportTab(user);
                tabbedPane.addTab("Support", supportTab);
            }

        }

        // --- ADMIN / AUDITOR / EMPLOYER ---

        if (user.canViewAllAccounts()) {
            AllAccountsTab allAccountsTab = new AllAccountsTab(user);
            tabbedPane.addTab("All Accounts", allAccountsTab);
            refreshableTabs.add(allAccountsTab);
        }

        if (user.canViewAllTransactionsHistory()) {
            AllTransactionsTab allTransactionsTab = new AllTransactionsTab();
            tabbedPane.addTab("All Transactions", allTransactionsTab);
            refreshableTabs.add(allTransactionsTab);
        }

        if (user.canPromoteUser() || user.canDemoteUser()
                || user.canRemoveUsers() || user instanceof BankEmployer) {

            UserManagementTab userManagementTab = new UserManagementTab(user);
            tabbedPane.addTab("User Management", userManagementTab);
            refreshableTabs.add(userManagementTab);
        }
        
        if (user.canAdvanceTime()) {
            TimeControlTab timeTab = new TimeControlTab(user);
            tabbedPane.addTab("Time Control", timeTab);
            refreshableTabs.add(timeTab);
        }

        SettingsTab settingsTab = new SettingsTab(user);
        tabbedPane.addTab("Settings", settingsTab);
        refreshableTabs.add(settingsTab);

        if (user.canAssistUsers()) {
            CustomerSupportTab customerSupportTab = new CustomerSupportTab(user);
            tabbedPane.addTab("Customer Support", customerSupportTab);
            refreshableTabs.add(customerSupportTab);
        }
    }

    /**
     * GLOBAL refresh όλων των tabs
     */
    public void refreshAllTabs() {
        for (Refreshable tab : refreshableTabs) {
            tab.refresh();
        }
    }
}
