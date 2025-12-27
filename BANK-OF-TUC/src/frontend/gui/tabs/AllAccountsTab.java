package frontend.gui.tabs;

import backend.BankSystem;
import backend.accounts.Account;
import backend.users.Admin;
import backend.users.Auditor;
import backend.users.Customer;
import backend.users.User;
import backend.users.ΒusinessCustomer;
import backend.users.BankEmployer;

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
    private User currentUser;
    private JButton closeBtn;

    public AllAccountsTab(User user) {
        // ΔΙΟΡΘΩΣΗ: Αποθηκεύουμε τον χρήστη ως User χωρίς επικίνδυνα casts
        this.currentUser = user;
        
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // --- ΠΑΝΩ ΜΕΡΟΣ: SEARCH BAR ---
        JPanel searchPanel = new JPanel(new BorderLayout(10, 10));
        searchPanel.add(new JLabel("Αναζήτηση (IBAN ή ID):"), BorderLayout.WEST);
        
        searchField = new JTextField();
        searchPanel.add(searchField, BorderLayout.CENTER);
        add(searchPanel, BorderLayout.NORTH);

        // --- ΚΕΝΤΡΟ: ΠΙΝΑΚΑΣ ---
        String[] columnNames = {"Owner ID", "Full Name", "IBAN", "Type", "Balance"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; 
            }
        };

        accountsTable = new JTable(tableModel);
        sorter = new TableRowSorter<>(tableModel);
        accountsTable.setRowSorter(sorter);
        
        add(new JScrollPane(accountsTable), BorderLayout.CENTER);

        // --- ΚΑΤΩ ΜΕΡΟΣ: ΚΟΥΜΠΙΑ ---
        closeBtn = new JButton("Close Selected Account");
        closeBtn.setBackground(new Color(180, 50, 50));
        closeBtn.setForeground(Color.WHITE);
        closeBtn.setFont(new Font("SansSerif", Font.BOLD, 12));
        
        // ΕΛΕΓΧΟΣ ΔΙΚΑΙΩΜΑΤΩΝ: Μόνο Admin και Employer βλέπουν το κουμπί διαγραφής
        if (currentUser instanceof Auditor) {
            closeBtn.setVisible(false);
        }

        closeBtn.addActionListener(e -> handleCloseAccount());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(closeBtn);
        add(buttonPanel, BorderLayout.SOUTH);

        // SEARCH LOGIC
        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String text = searchField.getText();
                sorter.setRowFilter(text.trim().isEmpty() ? null : RowFilter.regexFilter("(?i)" + text));
            }
        });

        refresh();
    }

    private void handleCloseAccount() {
        int selectedRow = accountsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Παρακαλώ επιλέξτε έναν λογαριασμό.");
            return;
        }

        int modelRow = accountsTable.convertRowIndexToModel(selectedRow);
        String userID = (String) tableModel.getValueAt(modelRow, 0);
        String iban = (String) tableModel.getValueAt(modelRow, 2);

        int confirm = JOptionPane.showConfirmDialog(this, 
            "Οριστική διαγραφή του λογαριασμού: " + iban + ";",
            "Επιβεβαίωση", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            BankSystem bank = BankSystem.getInstance();
            User targetUser = bank.getCustomers().get(userID);
            if (targetUser == null) targetUser = bank.getBusinessCustomers().get(userID);

            if (targetUser != null) {
                boolean success = false;
                
                // Ασφαλές Casting ανάλογα με τον τύπο του currentUser
                if (currentUser instanceof Admin admin) {
                    success = admin.deleteUserAccount(targetUser, iban);
                } else if (currentUser instanceof BankEmployer employer) {
                    success = employer.deleteUserAccount(targetUser, iban);
                }

                if (success) {
                	bank.dao.save(bank);
                    JOptionPane.showMessageDialog(this, "Ο λογαριασμός έκλεισε.");
                    refreshEntireSystem();
                } else {
                    JOptionPane.showMessageDialog(this, "Αδυναμία διαγραφής.", "Σφάλμα", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    public void refresh() {
        tableModel.setRowCount(0); 
        BankSystem bank = BankSystem.getInstance();
        
        // Φόρτωση όλων των τύπων πελατών
        if (bank.getCustomers() != null) {
            bank.getCustomers().values().forEach(this::addAccountsToTable);
        }
        if (bank.getBusinessCustomers() != null) {
            bank.getBusinessCustomers().values().forEach(this::addAccountsToTable);
        }
    }

    private void addAccountsToTable(User user) {
        // Χρήση της getAccounts() αν υπάρχει στη βασική κλάση User ή έλεγχος τύπου
        java.util.List<Account> accounts = new java.util.ArrayList<>();
        if (user instanceof Customer c) accounts = c.getAccounts();
        else if (user instanceof ΒusinessCustomer bc) accounts = bc.getAccounts();

        for (Account acc : accounts) {
            tableModel.addRow(new Object[]{
                user.getUserID(),
                user.getFullName(),
                acc.getIBAN(),
                acc.getClass().getSimpleName().replace("Account", ""),
                String.format("%.2f €", acc.getBalance())
            });
        }
    }

    private void refreshEntireSystem() {
        refresh();
        Container parent = getParent();
        while (parent != null && !(parent instanceof JTabbedPane)) {
            parent = parent.getParent();
        }

        if (parent instanceof JTabbedPane tabs) {
            for (int i = 0; i < tabs.getTabCount(); i++) {
                Component tab = tabs.getComponentAt(i);
                // Δυναμική κλήση refresh αν το tab το υποστηρίζει
                if (tab instanceof UserManagementTab) ((UserManagementTab) tab).refreshData();
                // Προσθέστε εδώ και άλλα tabs αν χρειάζεται
            }
        }
    }
}