package frontend.gui.tabs;

import backend.BankSystem;
import backend.accounts.Account;
import backend.users.Admin;
import backend.users.Customer;
import backend.users.User;
import backend.users.ΒusinessCustomer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Map;

public class AllAccountsTab extends JPanel {

    private JTable accountsTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private TableRowSorter<DefaultTableModel> sorter;
    private final Admin currentUser;

    /**
     * Constructor που δέχεται τον συνδεδεμένο User (Admin/Auditor)
     */
    public AllAccountsTab(User user) {
        this.currentUser = (Admin) user;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // --- ΠΑΝΩ ΜΕΡΟΣ: SEARCH BAR ---
        JPanel searchPanel = new JPanel(new BorderLayout(10, 10));
        searchPanel.add(new JLabel("Search (IBAN or Owner ID):"), BorderLayout.WEST);
        
        searchField = new JTextField();
        searchField.setToolTipText("Search across all columns...");
        searchPanel.add(searchField, BorderLayout.CENTER);
        add(searchPanel, BorderLayout.NORTH);

        // --- ΚΕΝΤΡΟ: ΠΙΝΑΚΑΣ ΜΕ SCROLL ---
        String[] columnNames = {"Owner ID", "Owner Name", "IBAN", "Account Type", "Balance (€)"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Μόνο ανάγνωση
            }
        };

        accountsTable = new JTable(tableModel);
        sorter = new TableRowSorter<>(tableModel);
        accountsTable.setRowSorter(sorter);
        
        // UI βελτιώσεις πίνακα
        accountsTable.setFillsViewportHeight(true);
        accountsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        accountsTable.getTableHeader().setReorderingAllowed(false);

        JScrollPane scrollPane = new JScrollPane(accountsTable);
        add(scrollPane, BorderLayout.CENTER);

        // --- ΚΑΤΩ ΜΕΡΟΣ: ΚΟΥΜΠΙ ΔΙΑΓΡΑΦΗΣ (CLOSE ACCOUNT) ---
        JButton closeBtn = new JButton("Close Selected Account");
        closeBtn.setBackground(new Color(180, 50, 50)); // Κόκκινο για κρίσιμη ενέργεια
        closeBtn.setForeground(Color.WHITE);
        closeBtn.setFont(new Font("SansSerif", Font.BOLD, 12));
        closeBtn.setFocusPainted(false);
        
        // Εμφάνιση του κουμπιού μόνο αν ο χρήστης έχει δικαίωμα (π.χ. Admin, όχι Auditor)
        // Αν ο Auditor δεν πρέπει να σβήνει, βάλε έλεγχο εδώ
        closeBtn.addActionListener(e -> handleCloseAccount());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(closeBtn);
        add(buttonPanel, BorderLayout.SOUTH);

        // --- LOGIC: SEARCH FILTER ---
        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String text = searchField.getText();
                if (text.trim().length() == 0) {
                    sorter.setRowFilter(null);
                } else {
                    sorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
                }
            }
        });

        refresh();
    }

    /**
     * Διαχειρίζεται το κλείσιμο του λογαριασμού και το refresh του GUI
     */
    private void handleCloseAccount() {
        int selectedRow = accountsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an account from the table.");
            return;
        }

        // Μετατροπή index από view σε model (απαραίτητο λόγω sorter)
        int modelRow = accountsTable.convertRowIndexToModel(selectedRow);
        String userID = (String) tableModel.getValueAt(modelRow, 0);
        String iban = (String) tableModel.getValueAt(modelRow, 2);

        int confirm = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to PERMANENTLY close account: " + iban + "?",
            "Confirm Account Closure", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            BankSystem bank = BankSystem.getInstance();
            
            // Εύρεση του αντικειμένου User (Customer ή Business)
            User targetUser = bank.getCustomers().get(userID);
            if (targetUser == null) {
                targetUser = bank.getBusinessCustomers().get(userID);
            }

            // Κλήση της μεθόδου διαγραφής στο BankSystem
            if (currentUser.deleteUserAccount(targetUser, iban)) {
                bank.saveAllData(); // Οριστική αποθήκευση στο JSON
                JOptionPane.showMessageDialog(this, "Account closed successfully.");
                
                // ΚΑΘΟΛΙΚΟ REFRESH: Ενημερώνει όλα τα tabs του Dashboard
                refreshEntireSystem();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to close account. Target user or account not found.", 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Ενημερώνει όλα τα tabs που βρίσκονται στο DashboardFrame
     */
    private void refreshEntireSystem() {
        // 1. Refresh αυτού του tab
        refresh();

        // 2. Refresh όλων των άλλων tabs που είναι στο JTabbedPane
        Container parent = getParent();
        while (parent != null && !(parent instanceof JTabbedPane)) {
            parent = parent.getParent();
        }

        if (parent instanceof JTabbedPane tabs) {
            for (int i = 0; i < tabs.getTabCount(); i++) {
                Component tab = tabs.getComponentAt(i);
                
                // Εδώ καλούμε τη refreshData για κάθε tab αν την έχει
                // (Πρέπει τα tabs σου να έχουν αυτή τη μέθοδο public)
                if (tab instanceof CustomerOverviewTab) ((CustomerOverviewTab) tab).refresh();
                if (tab instanceof MyAccountsTab) ((MyAccountsTab) tab).refresh();
                if (tab instanceof MyTransactionsTab) ((MyTransactionsTab) tab).refresh();
                //if (tab instanceof AllTransactionsTab) ((AllTransactionsTab) tab).refresh();
            }
        }
    }

    /**
     * Γεμίζει τον πίνακα με τα δεδομένα από το BankSystem
     */
    public void refresh() {
        tableModel.setRowCount(0); 
        BankSystem bank = BankSystem.getInstance();
        
        // Φόρτωση Απλών Πελατών
        Map<String, Customer> customers = bank.getCustomers();
        if (customers != null) {
            for (Customer c : customers.values()) {
                addAccountsToTable(c);
            }
        }

        // Φόρτωση Business Πελατών
        Map<String, ΒusinessCustomer> businessCustomers = bank.getBusinessCustomers();
        if (businessCustomers != null) {
            for (ΒusinessCustomer bc : businessCustomers.values()) {
                addAccountsToTable(bc);
            }
        }
    }

    /**
     * Προσθέτει τους λογαριασμούς ενός User (Customer/Business) στον πίνακα
     */
    private void addAccountsToTable(User user) {
        if (user instanceof Customer customer) {
            for (Account acc : customer.getAccounts()) {
                tableModel.addRow(new Object[]{
                    customer.getUserID(),
                    customer.getFullName(),
                    acc.getIBAN(),
                    acc.getClass().getSimpleName().replace("Account", ""),
                    String.format("%.2f €", acc.getBalance())
                });
            }
        } else if (user instanceof ΒusinessCustomer bCust) {
            for (Account acc : bCust.getAccounts()) {
                tableModel.addRow(new Object[]{
                    bCust.getUserID(),
                    bCust.getFullName(),
                    acc.getIBAN(),
                    acc.getClass().getSimpleName().replace("Account", ""),
                    String.format("%.2f €", acc.getBalance())
                });
            }
        }
    }
}