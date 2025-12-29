package frontend.gui.tabs;

import javax.swing.*;
import java.awt.*;
import backend.users.User;
import backend.accounts.Account;
import backend.support.Bill;
import backend.BankSystem;
import services.account_services.PayBillCommand;

public class BillPaymentTab extends JPanel {
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
        checkBtn.addActionListener(e -> {
            String code = codeField.getText().trim();
            foundBill = BankSystem.getInstance().findBillByCode(code);

            if (foundBill != null) {
                if (foundBill.isPaid()) {
                    JOptionPane.showMessageDialog(this, "Αυτός ο λογαριασμός έχει ήδη πληρωθεί.");
                    payBtn.setEnabled(false);
                } else {
                    detailsLabel.setText(String.format(
                        "<html><div style='background:white; padding:15px; border:1px solid #2e8b57;'>"
                        + "<b style='color:#2e8b57;'>Βρέθηκε Λογαριασμός!</b><br><br>"
                        + "<b>Επιχείρηση:</b> %s<br>"
                        + "<b>Ποσό:</b> %.2f€<br>"
                        + "<b>Περιγραφή:</b> %s</div></html>", 
                        foundBill.getBusinessName(), foundBill.getAmount(), foundBill.getDescription()
                    ));
                    payBtn.setEnabled(true);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Ο κωδικός RF δεν είναι έγκυρος ή δεν βρέθηκε.");
                payBtn.setEnabled(false);
            }
        });

        // --- Logic: Πληρωμή Λογαριασμού ---
        payBtn.addActionListener(e -> {
            // 1. Λήψη Κύριου Λογαριασμού
            Account acc = currentUser.getPrimaryAccount();
            if (acc == null) {
                JOptionPane.showMessageDialog(this, "Δεν βρέθηκε κύριος λογαριασμός για την πληρωμή.", "Σφάλμα", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // 2. Έλεγχος αν ο λογαριασμός είναι Frozen
            if (acc.isFrozen()) {
                JOptionPane.showMessageDialog(this, "Ο λογαριασμός σας είναι παγωμένος (Frozen). Δεν μπορείτε να πραγματοποιήσετε πληρωμές.", "Αδυναμία Συναλλαγής", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // 3. Έλεγχος Υπολοίπου
            double billAmount = foundBill.getAmount();
            double balance = acc.getBalance();

            if (balance < billAmount) {
                String errorMsg = String.format(
                    "Ανεπαρκές υπόλοιπο!\n\nΠοσό Λογαριασμού: %.2f€\nΔιαθέσιμο Υπόλοιπο: %.2f€\nΛείπουν: %.2f€",
                    billAmount, balance, (billAmount - balance)
                );
                JOptionPane.showMessageDialog(this, errorMsg, "Σφάλμα Υπολοίπου", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // 4. Επιβεβαίωση Χρήστη
            int confirm = JOptionPane.showConfirmDialog(this, 
                String.format("Επιβεβαιώνετε την πληρωμή %.2f€ προς '%s';", billAmount, foundBill.getBusinessName()), 
                "Επιβεβαίωση Πληρωμής", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    // Εκτέλεση μέσω του Command
                    new PayBillCommand(acc, foundBill).execute();
                    
                    JOptionPane.showMessageDialog(this, "Η πληρωμή ολοκληρώθηκε επιτυχώς!", "Επιτυχία", JOptionPane.INFORMATION_MESSAGE);
                    
                    // Καθαρισμός UI
                    payBtn.setEnabled(false);
                    codeField.setText("");
                    detailsLabel.setText("<html><b style='color:green;'>Η πληρωμή εκτελέστηκε επιτυχώς.</b></html>");
                    
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Σφάλμα κατά την πληρωμή: " + ex.getMessage(), "Σφάλμα Συστήματος", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
}