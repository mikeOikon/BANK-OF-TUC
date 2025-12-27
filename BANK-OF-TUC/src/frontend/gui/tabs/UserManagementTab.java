package frontend.gui.tabs;

import backend.BankSystem;
import backend.users.*;
import services.DeleteUserCommand;
import services.PromoteUserCommand;
import services.UserManager;
import types.UserType;

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

        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to delete user " + userId + "?",
            "Confirm Delete",
            JOptionPane.YES_NO_OPTION
        );

        if (confirm != JOptionPane.YES_OPTION) return;

        try {
            // Create the command
            DeleteUserCommand deleteCommand = new DeleteUserCommand(currentUser, userId);

            // Execute it via UserManager
            UserManager manager = new UserManager();
            manager.execute(deleteCommand);

            // Save data and refresh table
            BankSystem bank = BankSystem.getInstance();
            bank.dao.save(bank);
            refreshData();

            JOptionPane.showMessageDialog(this, "User deleted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);

        } catch (SecurityException se) {
            JOptionPane.showMessageDialog(this, se.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Failed to delete user: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    private void handlePromoteUser() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Παρακαλώ επιλέξτε έναν χρήστη από τον πίνακα.");
            return;
        }

        int modelRow = userTable.convertRowIndexToModel(selectedRow);
        String targetUserId = (String) tableModel.getValueAt(modelRow, 0);

        if (!currentUser.canPromoteUser()) {
            JOptionPane.showMessageDialog(this, "Μόνο οι Διαχειριστές έχουν πρόσβαση σε αυτή τη λειτουργία.", "Απαγόρευση", JOptionPane.ERROR_MESSAGE);
            return;
        }

        BankSystem bank = BankSystem.getInstance();
        User oldUser = bank.getUserById(targetUserId);

        // Επιλογή τύπου προαγωγής μόνο στο GUI
        UserType targetType = null;
        if (oldUser.getUserType() == UserType.CUSTOMER) {
            String[] options = {"Bank Employee", "Auditor"};
            int choice = JOptionPane.showOptionDialog(
                    this,
                    "Επιλέξτε τον τύπο χρήστη για προαγωγή:",
                    "Προαγωγή Χρήστη",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    options[0]
            );
            if (choice == 0) targetType = UserType.EMPLOYEE;
            else if (choice == 1) targetType = UserType.AUDITOR;
            else return; // ακύρωση
        } else if (oldUser.getUserType() == UserType.EMPLOYEE) {
            targetType = UserType.ADMIN;
        } else {
            JOptionPane.showMessageDialog(this, "Αυτός ο χρήστης δεν μπορεί να προαχθεί.", "Σφάλμα", JOptionPane.ERROR_MESSAGE);
            return;
        }

        PromoteUserCommand promoteCommand = new PromoteUserCommand(bank, currentUser, oldUser, targetType);
        UserManager userManager = new UserManager();

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Είστε σίγουροι ότι θέλετε να προάγετε τον χρήστη " + oldUser.getFullName() + "?",
                "Επιβεβαίωση Προαγωγής", JOptionPane.YES_NO_OPTION
        );

        if (confirm != JOptionPane.YES_OPTION) return;

        try {
            userManager.execute(promoteCommand);
            JOptionPane.showMessageDialog(this, "Η προαγωγή ολοκληρώθηκε επιτυχώς!");
            refreshData();
        } catch (SecurityException se) {
            JOptionPane.showMessageDialog(this, se.getMessage(), "Σφάλμα Ασφαλείας", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Αποτυχία προαγωγής: " + e.getMessage(), "Σφάλμα", JOptionPane.ERROR_MESSAGE);
        }
    }

    
}