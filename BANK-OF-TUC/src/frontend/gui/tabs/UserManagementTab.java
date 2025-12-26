package frontend.gui.tabs;

import backend.BankSystem;
import backend.users.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Map;

public class UserManagementTab extends JPanel {

    private final User currentUser; // Αλλαγή σε User για να δέχεται Admin & BankEmployer
    private JTable userTable;
    private DefaultTableModel tableModel;
    private JButton deleteBtn;
    private JButton promoteBtn;

    public UserManagementTab(User user) {
        // Αποφεύγουμε το ClassCastException αποθηκεύοντας ως User
        this.currentUser = user;

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // --- ΠΙΝΑΚΑΣ ΧΡΗΣΤΩΝ ---
        String[] columns = {"User ID", "Username", "Full Name", "Role"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        userTable = new JTable(tableModel);
        add(new JScrollPane(userTable), BorderLayout.CENTER);

        // --- PANEL ΚΟΥΜΠΙΩΝ ---
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        promoteBtn = new JButton("Promote User");
        deleteBtn = new JButton("Delete User");
        deleteBtn.setBackground(new Color(180, 50, 50));
        deleteBtn.setForeground(Color.WHITE);

        // --- ΕΛΕΓΧΟΣ ΔΙΚΑΙΩΜΑΤΩΝ ---
        // Αν ο χρήστης είναι απλός υπάλληλος, κρύβουμε το κουμπί διαγραφής
        if (currentUser instanceof BankEmployer && !(currentUser instanceof Admin)) {
            deleteBtn.setVisible(false);
        }

        buttonPanel.add(promoteBtn);
        buttonPanel.add(deleteBtn);
        add(buttonPanel, BorderLayout.SOUTH);

        // Listeners
        deleteBtn.addActionListener(e -> handleDeleteUser());
        promoteBtn.addActionListener(e -> handlePromoteUser());

        refreshData();
    }

    public void refreshData() {
        tableModel.setRowCount(0);
        BankSystem bank = BankSystem.getInstance();

        // Προσθήκη όλων των χρηστών στον πίνακα (εκτός του εαυτού μας)
        addUserToTable(bank.getCustomers());
        addUserToTable(bank.getBusinessCustomers());
        addUserToTable(bank.getBankEmployers());
        addUserToTable(bank.getAuditors());
        addUserToTable(bank.getAdmins());
    }

    private void addUserToTable(Map<String, ? extends User> users) {
        if (users == null) return;
        for (User u : users.values()) {
            // Κρύβουμε τον συνδεδεμένο λογαριασμό από τη λίστα για ασφάλεια
            if (u.getUserID().equals(currentUser.getUserID())) continue;

            tableModel.addRow(new Object[]{
                u.getUserID(),
                u.getUsername(),
                u.getFullName(),
                u.getClass().getSimpleName()
            });
        }
    }

    private void handleDeleteUser() {
        int row = userTable.getSelectedRow();
        if (row == -1) return;

        String userId = (String) tableModel.getValueAt(row, 0);
        
        // Μόνο οι Admins μπορούν να διαγράφουν
        if (currentUser instanceof Admin admin) {
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete user " + userId + "?");
            if (confirm == JOptionPane.YES_OPTION) {
                // Εδώ καλείς τη μέθοδο διαγραφής από το backend
                // π.χ. BankSystem.getInstance().removeUser(userId);
                refreshData();
            }
        }
    }

    private void handlePromoteUser() {
        // Λογική για προαγωγή χρήστη (μπορεί να την κάνει και ο Employer)
        JOptionPane.showMessageDialog(this, "Promotion logic goes here.");
    }
}