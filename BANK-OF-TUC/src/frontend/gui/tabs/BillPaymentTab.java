package frontend.gui.tabs;

import javax.swing.*;
import java.awt.*;
import backend.users.User;
import backend.accounts.Account;
import backend.support.Bill;
import backend.BankSystem;
import services.account_services.PayBillCommand;

// Προσθήκη του implements Refreshable για να ακούει στο refresh του Dashboard
public class BillPaymentTab extends JPanel implements Refreshable{
    private JTextField codeField;
    private JLabel detailsLabel;
    private JButton payBtn;
    private User currentUser;
    private Bill foundBill;

    public BillPaymentTab(User user) {
        this.currentUser = user;
        setLayout(new BorderLayout(20, 20));
        setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        // --- Header ---
        JLabel title = new JLabel("Ηλεκτρονική Πληρωμή Λογαριασμού (RF)");
        title.setFont(new Font("SansSerif", Font.BOLD, 22));
        add(title, BorderLayout.NORTH);

        // --- Central Panel ---
        JPanel centralPanel = new JPanel();
        centralPanel.setLayout(new BoxLayout(centralPanel, BoxLayout.Y_AXIS));

        codeField = new JTextField(20);
        codeField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        codeField.setFont(new Font("Monospaced", Font.BOLD, 16));
        
        JButton checkBtn = new JButton("Αναζήτηση Λογαριασμού");
        
        detailsLabel = new JLabel("<html><i>Εισάγετε τον κωδικό πληρωμής για να δείτε τις λεπτομέρειες.</i></html>");
        detailsLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        detailsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        payBtn = new JButton("Επιβεβαίωση & Πληρωμή");
        payBtn.setEnabled(false);
        payBtn.setBackground(new Color(46, 139, 87));
        payBtn.setForeground(Color.WHITE);
        payBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        payBtn.setPreferredSize(new Dimension(200, 40));

        centralPanel.add(new JLabel("Κωδικός Πληρωμής (RF):"));
        centralPanel.add(codeField);
        centralPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        centralPanel.add(checkBtn);
        centralPanel.add(detailsLabel);
        centralPanel.add(payBtn);

        add(centralPanel, BorderLayout.CENTER);

        // --- Logic: Αναζήτηση Λογαριασμού ---
        checkBtn.addActionListener(e -> searchForBill());

        // --- Logic: Πληρωμή Λογαριασμού ---
        payBtn.addActionListener(e -> {
            Account acc = currentUser.getPrimaryAccount();
            if (acc == null) {
                JOptionPane.showMessageDialog(this, "Δεν βρέθηκε κύριος λογαριασμός.", "Σφάλμα", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (acc.isFrozen()) {
                JOptionPane.showMessageDialog(this, "Ο λογαριασμός είναι Frozen.", "Αδυναμία Συναλλαγής", JOptionPane.WARNING_MESSAGE);
                return;
            }

            double billAmount = foundBill.getAmount();
            double balance = acc.getBalance();

            if (balance < billAmount) {
                String errorMsg = String.format("Ανεπαρκές υπόλοιπο!\nΠοσό: %.2f€\nΥπόλοιπο: %.2f€", billAmount, balance);
                JOptionPane.showMessageDialog(this, errorMsg, "Σφάλμα", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(this, 
                String.format("Επιβεβαιώνετε την πληρωμή %.2f€;", billAmount), 
                "Επιβεβαίωση", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    new PayBillCommand(acc, foundBill).execute();
                    JOptionPane.showMessageDialog(this, "Η πληρωμή ολοκληρώθηκε!", "Επιτυχία", JOptionPane.INFORMATION_MESSAGE);
                    
                    // Μετά την πληρωμή, κάνουμε refresh τα δεδομένα μας
                    refreshData();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Σφάλμα: " + ex.getMessage(), "Σφάλμα", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    /**
     * Η μέθοδος που αναζητά το Bill. 
     * Την απομονώσαμε για να καλείται και από το refresh.
     */
    private void searchForBill() {
        String code = codeField.getText().trim();
        if (code.isEmpty()) return;

        foundBill = BankSystem.getInstance().findBillByCode(code);

        if (foundBill != null) {
            if (foundBill.isPaid()) {
                detailsLabel.setText("<html><b style='color:red;'>Ο λογαριασμός έχει ήδη πληρωθεί.</b></html>");
                payBtn.setEnabled(false);
            } else {
                detailsLabel.setText(String.format(
                    "<html><div style='background:white; padding:15px; border:1px solid #2e8b57;'>"
                    + "<b>Επιχείρηση:</b> %s<br><b>Ποσό:</b> %.2f€</div></html>", 
                    foundBill.getBusinessName(), foundBill.getAmount()
                ));
                payBtn.setEnabled(true);
            }
        } else {
            detailsLabel.setText("<html><i>Ο κωδικός δεν βρέθηκε.</i></html>");
            payBtn.setEnabled(false);
        }
    }

    /**
     * Υλοποίηση του Refreshable (αντίστοιχο με το AllTransactionsTab)
     */
    @Override
    public void refresh() {
        refreshData();
    }

    public void refreshData() {
        // Αν έχουμε ήδη βρει έναν λογαριασμό, ξανατσεκάρουμε την κατάστασή του
        // (π.χ. μήπως πληρώθηκε από άλλη συσκευή/tab)
        if (!codeField.getText().trim().isEmpty()) {
            searchForBill();
        } else {
            // Αν το πεδίο είναι άδειο, απλά μηδενίζουμε το UI
            payBtn.setEnabled(false);
            detailsLabel.setText("<html><i>Εισάγετε κωδικό RF.</i></html>");
        }
        
        // Επανυπολογισμός των γραφικών στοιχείων
        revalidate();
        repaint();
    }
}