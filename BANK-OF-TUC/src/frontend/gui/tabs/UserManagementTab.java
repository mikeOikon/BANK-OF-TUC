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
            promoteBtn.setVisible(false); 
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
            	BankSystem.getInstance().removeUser(userId);
                BankSystem.getInstance().saveAllData(); 
                refreshData();
            }
        }
    }

    private void handlePromoteUser() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Παρακαλώ επιλέξτε έναν χρήστη από τον πίνακα.");
            return;
        }

        // 1. Λήψη του ID από τον πίνακα
        int modelRow = userTable.convertRowIndexToModel(selectedRow);
        String targetUserId = (String) tableModel.getValueAt(modelRow, 0);

        // 2. Έλεγχος αν ο συνδεδεμένος χρήστης είναι Admin
        if (!(currentUser instanceof Admin admin)) {
            JOptionPane.showMessageDialog(this, "Μόνο οι Διαχειριστές έχουν πρόσβαση σε αυτή τη λειτουργία.", "Απαγόρευση", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 3. Εύρεση του αντικειμένου User από το BankSystem
        BankSystem bank = BankSystem.getInstance();
        User oldUser = bank.getUserById(targetUserId);
        
        if (!(oldUser instanceof Customer)) {
            JOptionPane.showMessageDialog(this,
                    "Μόνο πελάτες μπορούν να προαχθούν.",
                    "Μη επιτρεπτή ενέργεια",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }


        if (oldUser == null) {
            JOptionPane.showMessageDialog(this, "Ο χρήστης δεν βρέθηκε στο σύστημα.");
            return;
        }

        // 4. Επιλογή Νέου Ρόλου (Οι τιμές πρέπει να αντιστοιχούν στο UserType Enum)
        // Αν το Enum σου έχει τιμές όπως CUSTOMER, BUSINESS_CUSTOMER κλπ.
        String[] roles = {"BANK_EMPLOYER", "AUDITOR"};
        String selectedRole = (String) JOptionPane.showInputDialog(this, 
                "Επιλέξτε νέο ρόλο για τον χρήστη " + oldUser.getFullName() + ":", 
                "Προαγωγή Χρήστη", 
                JOptionPane.QUESTION_MESSAGE, 
                null, roles, roles[0]);

        if (selectedRole == null) return; // Cancel

        // 5. Επιβεβαίωση
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Είστε σίγουροι ότι θέλετε να αλλάξετε τον ρόλο του χρήστη σε " + selectedRole + ";\n" +
            "Προσοχή: Θα δημιουργηθεί νέο ID συστήματος.", 
            "Επιβεβαίωση Αλλαγής", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            // Κλήση της μεθόδου promoteUser που έφτιαξες στον Admin
            boolean success = admin.promoteUser(oldUser, selectedRole);

            if (success) {
                JOptionPane.showMessageDialog(this, "Η προαγωγή ολοκληρώθηκε επιτυχώς!");
                refreshData(); // Ανανέωση του πίνακα
            } else {
                JOptionPane.showMessageDialog(this, "Αποτυχία προαγωγής. Ελέγξτε το Log του συστήματος.", "Σφάλμα", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
}