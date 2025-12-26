package frontend.gui;

import backend.users.*;
import frontend.gui.tabs.*;
import javax.swing.*;
import java.awt.*;

public class DashboardFrame extends JFrame {

    private JTabbedPane tabs;
    private final User currentUser;

    public DashboardFrame() {
        // Παίρνουμε τον χρήστη από το Session
        this.currentUser = UserSession.getInstance().getCurrentUser();

        if (currentUser == null) {
            dispose();
            new StartFrame().setVisible(true);
            return;
        }

        setTitle("Bank of TUC — Dashboard [" + currentUser.getUsername() + "]");
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Κύριο Panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        
        // Header Panel
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(0, 51, 102));
        header.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JLabel welcomeLabel = new JLabel("Καλωσήρθατε, " + currentUser.getFullName() + " (" + currentUser.getClass().getSimpleName() + ")");
        welcomeLabel.setForeground(Color.WHITE);
        welcomeLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        header.add(welcomeLabel, BorderLayout.WEST);

        JButton logoutBtn = new JButton("Αποσύνδεση");
        logoutBtn.addActionListener(e -> handleLogout());
        header.add(logoutBtn, BorderLayout.EAST);

        mainPanel.add(header, BorderLayout.NORTH);

        // Δημιουργία των Tabs
        tabs = new JTabbedPane();
        buildTabs();
        mainPanel.add(tabs, BorderLayout.CENTER);

        add(mainPanel);
    }

    private void buildTabs() {
        // --- TABS ΓΙΑ ΠΕΛΑΤΕΣ (Customer / BusinessCustomer) ---
        if (currentUser instanceof Customer) {
            Customer customer = (Customer) currentUser;
            CustomerOverviewTab overview = new CustomerOverviewTab(customer);
            tabs.addTab("Overview", overview);
            tabs.addTab("My Accounts", new MyAccountsTab(customer, overview));
            // tabs.addTab("Transactions", new MyTransactionsTab(customer));
        }

        // --- TABS ΓΙΑ ΠΡΟΣΩΠΙΚΟ (Admin, Auditor, BankEmployer) ---
        
        // 1. Προβολή όλων των λογαριασμών (Ορατό σε όλους τους υπαλλήλους/admin)
        if (currentUser instanceof Admin || currentUser instanceof Auditor || currentUser instanceof BankEmployer) {
            tabs.addTab("Όλοι οι Λογαριασμοί", new AllAccountsTab(currentUser));
        }

        // 2. Διαχείριση Χρηστών (Ορατό σε Admin και BankEmployer - Ο Auditor εξαιρείται συνήθως)
        if (currentUser instanceof Admin || currentUser instanceof BankEmployer) {
            tabs.addTab("Διαχείριση Χρηστών", new UserManagementTab(currentUser));
        }

        // 3. Στατιστικά & Έλεγχος (Ορατό σε Admin και Auditor)
        if (currentUser instanceof Admin || currentUser instanceof Auditor) {
            // tabs.addTab("Audit Logs", new AuditLogsTab(currentUser));
        }
    }

    private void handleLogout() {
        int confirm = JOptionPane.showConfirmDialog(this, "Θέλετε να αποσυνδεθείτε;", "Logout", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            UserSession.getInstance().setCurrentUser(null);
            dispose();
            new StartFrame().setVisible(true);
        }
    }
}