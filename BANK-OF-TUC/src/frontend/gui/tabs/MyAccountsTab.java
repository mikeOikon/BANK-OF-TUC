package frontend.gui.tabs;

import backend.BankSystem;
import backend.accounts.Account;
import backend.users.BusinessCustomer;
import backend.users.Customer;
import backend.users.User;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class MyAccountsTab extends JPanel {

    private final User customer;
    private final JList<Account> accountList;
    private Account selectedAccount; // Αναφορά για χρήση μεταξύ μεθόδων
    private final JButton viewOverviewButton;
    private final JButton setPrimaryButton;
    private final CustomerOverviewTab overviewTab;

    public MyAccountsTab(User user, CustomerOverviewTab overviewTab) {
        this.customer = user;
        this.overviewTab = overviewTab;

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // --- ΛΙΣΤΑ ΛΟΓΑΡΙΑΣΜΩΝ ---
        accountList = new JList<>();
        accountList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        accountList.setFont(new Font("Monospaced", Font.PLAIN, 14));
        
        updateListModel();
        
        add(new JScrollPane(accountList), BorderLayout.CENTER);

        // Listener για την ενημέρωση της επιλογής
        accountList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                selectedAccount = accountList.getSelectedValue();
            }
        });

        // --- PANEL ΚΟΥΜΠΙΩΝ ---
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        
        viewOverviewButton = new JButton("View Overview");
        setPrimaryButton = new JButton("Set as Primary");
        
        buttonPanel.add(viewOverviewButton);
        buttonPanel.add(setPrimaryButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // --- ACTION LISTENERS ---

        // Listener για το Set as Primary
        setPrimaryButton.addActionListener(e -> {
            // Χρήση τοπικής μεταβλητής για να αποφύγουμε NPE αν η λίστα αλλάξει κατά το refresh
            Account currentSelection = accountList.getSelectedValue();

            if (currentSelection == null) {
                JOptionPane.showMessageDialog(this, 
                    "Please select an account first.", 
                    "Selection Required", 
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // 1. Ενημέρωση Backend
            if(this.customer instanceof Customer) {
            	Customer customer = (Customer)this.customer;
            	customer.setPrimaryAccount(currentSelection);
            }
            else {
            	BusinessCustomer bCustomer = (BusinessCustomer)this.customer;
            	bCustomer.setPrimaryAccount(currentSelection);
            }
            
            BankSystem bank= BankSystem.getInstance();
            bank.dao.save(bank); 
            
            // 2. Αποθήκευση στοιχείων πριν το refresh του μοντέλου
            String iban = currentSelection.getIBAN();
            
            // 3. Ανανέωση UI
            updateListModel(); 
            
            // 4. Ενημέρωση του Overview Tab ώστε να δείξει το νέο Status αμέσως
            overviewTab.setSelectedAccount(currentSelection);
            
            JOptionPane.showMessageDialog(this, 
                "Account " + iban + " is now set as primary.", 
                "Success", 
                JOptionPane.INFORMATION_MESSAGE);
        });

        // Listener για το View Overview
        viewOverviewButton.addActionListener(e -> {
            Account currentSelection = accountList.getSelectedValue();
            
            if (currentSelection == null) {
                JOptionPane.showMessageDialog(this, "Select an account first.");
                return;
            }
            
            overviewTab.setSelectedAccount(currentSelection);
            
            Container parent = MyAccountsTab.this.getParent();
            if (parent instanceof JTabbedPane) {
                ((JTabbedPane) parent).setSelectedComponent(overviewTab);
            }
        });
    }

    public void refresh() {
        updateListModel();
    }

    /**
     * Ενημερώνει το μοντέλο της λίστας και διατηρεί την επιλογή αν είναι δυνατόν.
     */
    private void updateListModel() {
        // Αποθήκευση της τρέχουσας επιλογής πριν το clear
        Account previouslySelected = accountList.getSelectedValue();
        
        DefaultListModel<Account> model = new DefaultListModel<>();
        ArrayList<Account> sortedAccounts = new ArrayList<>(customer.getAccounts());
        
        // Ταξινόμηση: Primary λογαριασμοί στην κορυφή
        sortedAccounts.sort((a, b) -> Boolean.compare(b.isPrimary(), a.isPrimary()));

        for (Account acc : sortedAccounts) {
            model.addElement(acc);
        }
        
        accountList.setModel(model);

        // Επαναφορά της επιλογής αν ο λογαριασμός υπάρχει ακόμα στη λίστα
        if (previouslySelected != null) {
            accountList.setSelectedValue(previouslySelected, true);
        }
    }
}