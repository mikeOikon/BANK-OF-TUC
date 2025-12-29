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

        // Header
        JLabel title = new JLabel("Ηλεκτρονική Πληρωμή Λογαριασμού (RF)");
        title.setFont(new Font("SansSerif", Font.BOLD, 22));
        add(title, BorderLayout.NORTH);

        // Center Panel
        JPanel centralPanel = new JPanel();
        centralPanel.setLayout(new BoxLayout(centralPanel, BoxLayout.Y_AXIS));

        codeField = new JTextField(20);
        codeField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        
        JButton checkBtn = new JButton("Αναζήτηση Λογαριασμού");
        detailsLabel = new JLabel("Εισάγετε τον κωδικό πληρωμής για να δείτε τις λεπτομέρειες.");
        detailsLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        payBtn = new JButton("Επιβεβαίωση & Πληρωμή");
        payBtn.setEnabled(false);
        payBtn.setBackground(new Color(46, 139, 87));
        payBtn.setForeground(Color.WHITE);

        centralPanel.add(new JLabel("Κωδικός Πληρωμής:"));
        centralPanel.add(codeField);
        centralPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        centralPanel.add(checkBtn);
        centralPanel.add(detailsLabel);
        centralPanel.add(payBtn);

        add(centralPanel, BorderLayout.CENTER);

        // Listeners
        checkBtn.addActionListener(e -> {
            String code = codeField.getText().trim();
            foundBill = BankSystem.getInstance().findBillByCode(code);

            if (foundBill != null && !foundBill.isPaid()) {
                detailsLabel.setText("<html><div style='background:white; padding:10px; border:1px solid #ccc;'>"
                        + "<b>Επιχείρηση:</b> " + foundBill.getBusinessName() + "<br>"
                        + "<b>Ποσό:</b> " + String.format("%.2f€", foundBill.getAmount()) + "<br>"
                        + "<b>Περιγραφή:</b> " + foundBill.getDescription() + "</div></html>");
                payBtn.setEnabled(true);
            } else if (foundBill != null && foundBill.isPaid()) {
                JOptionPane.showMessageDialog(this, "Αυτός ο λογαριασμός έχει ήδη πληρωθεί.");
            } else {
                JOptionPane.showMessageDialog(this, "Ο κωδικός δεν βρέθηκε.");
            }
        });

        payBtn.addActionListener(e -> {
            Account acc = currentUser.getPrimaryAccount();
            if (acc == null) {
                JOptionPane.showMessageDialog(this, "Δεν βρέθηκε κύριος λογαριασμός για την πληρωμή.");
                return;
            }
            
            int confirm = JOptionPane.showConfirmDialog(this, "Είστε σίγουροι για την πληρωμή " + foundBill.getAmount() + "€;");
            if (confirm == JOptionPane.YES_OPTION) {
                new PayBillCommand(acc, foundBill).execute();
                JOptionPane.showMessageDialog(this, "Η πληρωμή ολοκληρώθηκε επιτυχώς!");
                payBtn.setEnabled(false);
                codeField.setText("");
                detailsLabel.setText("Η πληρωμή εκτελέστηκε.");
            }
        });
    }
}