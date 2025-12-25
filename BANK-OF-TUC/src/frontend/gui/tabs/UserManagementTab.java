package frontend.gui.tabs;

import backend.BankSystem;
import backend.users.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UserManagementTab extends JPanel {

    private JTable userTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private TableRowSorter<DefaultTableModel> sorter;
    private Admin admin;

    public UserManagementTab(User user) {
    	this.admin = (Admin) user;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // --- ΠΑΝΩ ΜΕΡΟΣ: ΔΥΝΑΜΙΚΗ ΑΝΑΖΗΤΗΣΗ ---
        JPanel searchPanel = new JPanel(new BorderLayout(5, 5));
        searchPanel.add(new JLabel("Search User (ID):"), BorderLayout.WEST);
        searchField = new JTextField();
        searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { filter(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { filter(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { filter(); }
        });
        searchPanel.add(searchField, BorderLayout.CENTER);
        add(searchPanel, BorderLayout.NORTH);

        // --- ΚΕΝΤΡΟ: ΠΙΝΑΚΑΣ ΧΡΗΣΤΩΝ ---
        String[] columns = {"User ID", "Full Name", "Current Role"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };
        userTable = new JTable(tableModel);
        sorter = new TableRowSorter<>(tableModel);
        userTable.setRowSorter(sorter);
        add(new JScrollPane(userTable), BorderLayout.CENTER);

        // --- ΚΑΤΩ ΜΕΡΟΣ: ACTION BUTTONS ---
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        JButton promoteEmpBtn = new JButton("Promote to Employee");
        JButton promoteAudBtn = new JButton("Promote to Auditor");
        JButton deleteBtn = new JButton("Delete User");
        deleteBtn.setBackground(new Color(180, 50, 50));
        deleteBtn.setForeground(Color.WHITE);

        actionPanel.add(promoteEmpBtn);
        actionPanel.add(promoteAudBtn);
        actionPanel.add(new JSeparator(JSeparator.VERTICAL));
        actionPanel.add(deleteBtn);
        add(actionPanel, BorderLayout.SOUTH);

        // Listeners
        promoteEmpBtn.addActionListener(e -> handlePromotion("Employee"));
        promoteAudBtn.addActionListener(e -> handlePromotion("Auditor"));
        deleteBtn.addActionListener(e -> handleDeleteUser());

        refreshData();
    }

    private void filter() {
        sorter.setRowFilter(RowFilter.regexFilter("(?i)" + searchField.getText(), 0));
    }

    public void refreshData() {
        tableModel.setRowCount(0); 
        BankSystem bank = BankSystem.getInstance();
        
        // Προσθέτουμε όλους τους χάρτες
        addUserToTable(bank.getCustomers());
        addUserToTable(bank.getBusinessCustomers());
        addUserToTable(bank.getBankEmployers()); 
        addUserToTable(bank.getAuditors());
        addUserToTable(bank.getAdmins()); // Εδώ περιλαμβάνεται και ο εαυτός σου
    }

    private void addUserToTable(Map<String, ? extends User> users) {
        if (users == null) return;
        
        for (User u : users.values()) {
            // ΕΛΕΓΧΟΣ: Αν το ID του χρήστη είναι ίδιο με το ID του συνδεδεμένου Admin, προσπέρασέ τον
            if (u.getUserID().equals(this.admin.getUserID())) {
                continue; 
            }
            
            tableModel.addRow(new Object[]{
                u.getUserID(),
                u.getFullName(),
                u.getClass().getSimpleName()
            });
        }
    }

    private void handlePromotion(String newRole) {
        int row = userTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a user first.");
            return;
        }

        int modelRow = userTable.convertRowIndexToModel(row);
        String userID = (String) tableModel.getValueAt(modelRow, 0);
        String currentRole = (String) tableModel.getValueAt(modelRow, 2); // Παίρνει το SimpleName της κλάσης

        // 1. Έλεγχος για προαγωγή στον ίδιο ρόλο (Normalization)
        String normalizedCurrentRole = currentRole;
        if (currentRole.equals("BankEmployer")) normalizedCurrentRole = "Employee";

        if (normalizedCurrentRole.equalsIgnoreCase(newRole)) {
            JOptionPane.showMessageDialog(this, "User is already a(n) " + newRole + ".");
            return;
        }

        // 2. Security Policy: Auditor -> Employee
        if (currentRole.equals("Auditor") && newRole.equals("Employee")) {
            JOptionPane.showMessageDialog(this, "Security Policy: Auditors cannot be demoted to Employees.");
            return;
        }

        // 3. Νέος περιορισμός: Employer -> Auditor
        if (currentRole.equals("BankEmployer") && newRole.equals("Auditor")) {
            JOptionPane.showMessageDialog(this, "Security Policy: Bank Employers cannot be promoted to Auditors for integrity reasons.");
            return;
        }

        // --- Διαδικασία Επιβεβαίωσης ---
        int confirm = JOptionPane.showConfirmDialog(this, "Promote " + userID + " to " + newRole + "?");
        if (confirm == JOptionPane.YES_OPTION) {
            User oldUser = findUser(userID);
            if (oldUser != null) {
                boolean success = admin.promoteUser(oldUser, newRole);
                if (success) {
                    JOptionPane.showMessageDialog(this, "User promoted successfully!");
                    refreshData();
                }
            }
        }
    }
    
    private void handleDeleteUser() {
        int row = userTable.getSelectedRow();
        if (row == -1) return;

        int modelRow = userTable.convertRowIndexToModel(row);
        String userID = (String) tableModel.getValueAt(modelRow, 0);

        int confirm = JOptionPane.showConfirmDialog(this, "Permanently delete user " + userID + "?", "Warning", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (admin.removeUser(userID)) {
                refreshData();
                JOptionPane.showMessageDialog(this, "User removed.");
            }
        }
    }

    private User findUser(String id) {
        BankSystem bank = BankSystem.getInstance();
        User u = bank.getCustomers().get(id);
        if (u == null) u = bank.getBusinessCustomers().get(id);
        return u;
    }
}