package frontend.gui.tabs;

import javax.swing.*;
import java.awt.*;

import backend.users.User;
import frontend.gui.DashboardFrame;
import backend.accounts.Account;
import backend.support.Bill;
import backend.BankSystem;
import services.account_services.PayBillCommand;

public class BillPaymentTab extends JPanel implements Refreshable {

    private JTextField codeField;
    private JLabel detailsLabel;
    private JButton payBtn;
    private JCheckBox monthlyAutoPayCheck;

    private User currentUser;
    private Bill foundBill;
    private DashboardFrame dashboard;

    public BillPaymentTab(User user, DashboardFrame dashboard) {
        this.currentUser = user;
        this.dashboard = dashboard;

        setLayout(new BorderLayout(20, 20));
        setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        // --- Header ---
        JLabel title = new JLabel("Ηλεκτρονική Πληρωμή & Διαχείριση Συνδρομών");
        title.setFont(new Font("SansSerif", Font.BOLD, 22));
        add(title, BorderLayout.NORTH);

        // --- Central Panel ---
        JPanel centralPanel = new JPanel();
        centralPanel.setLayout(new BoxLayout(centralPanel, BoxLayout.Y_AXIS));

        codeField = new JTextField(20);
        codeField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        codeField.setFont(new Font("Monospaced", Font.BOLD, 16));

        JButton checkBtn = new JButton("Αναζήτηση Λογαριασμού");

        detailsLabel = new JLabel("<html><i>Εισάγετε κωδικό RF.</i></html>");
        detailsLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        detailsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        monthlyAutoPayCheck = new JCheckBox("Ενεργοποίηση πάγιας εντολής");
        monthlyAutoPayCheck.setVisible(false);
        monthlyAutoPayCheck.setAlignmentX(Component.CENTER_ALIGNMENT);

        payBtn = new JButton("Επιβεβαίωση & Πληρωμή");
        payBtn.setEnabled(false);
        payBtn.setBackground(new Color(46, 139, 87));
        payBtn.setForeground(Color.WHITE);
        payBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        payBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        centralPanel.add(new JLabel("Κωδικός Πληρωμής (RF):"));
        centralPanel.add(codeField);
        centralPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        centralPanel.add(checkBtn);
        centralPanel.add(detailsLabel);
        centralPanel.add(monthlyAutoPayCheck);
        centralPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        centralPanel.add(payBtn);

        add(centralPanel, BorderLayout.CENTER);

        // --- Logic ---
        checkBtn.addActionListener(e -> searchForBill());
        payBtn.addActionListener(e -> handlePayment());

        // ✅ Νέα Λογική: Αν αλλάξει το checkbox σε ήδη πληρωμένο λογαριασμό, ενημέρωσε τη συνδρομή
        monthlyAutoPayCheck.addActionListener(e -> {
            if (foundBill != null && foundBill.isPaid() && foundBill.isMonthly()) {
                handleToggleAutoPayOnly();
            }
        });
    }

    private void handleToggleAutoPayOnly() {
        Account acc = currentUser.getPrimaryAccount();
        if (acc == null) {
            JOptionPane.showMessageDialog(this, "Δεν βρέθηκε λογαριασμός χρέωσης.");
            return;
        }

        if (monthlyAutoPayCheck.isSelected()) {
            BankSystem.getInstance().enableMonthlyAutoPay(foundBill.getSubscriptionId(), acc.getIBAN());
            JOptionPane.showMessageDialog(this, "Η πάγια εντολή ενεργοποιήθηκε επιτυχώς!");
        } else {
            BankSystem.getInstance().disableMonthlyAutoPay(foundBill.getSubscriptionId());
            JOptionPane.showMessageDialog(this, "Η πάγια εντολή απενεργοποιήθηκε.");
        }
        refreshData();
    }

    private void handlePayment() {
        if (foundBill == null) return;
        Account acc = currentUser.getPrimaryAccount();
        
        if (acc == null || acc.isFrozen()) {
            JOptionPane.showMessageDialog(this, "Σφάλμα λογαριασμού.", "Σφάλμα", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            new PayBillCommand(acc, foundBill).execute();

            if (foundBill.isMonthly() && monthlyAutoPayCheck.isSelected()) {
                BankSystem.getInstance().enableMonthlyAutoPay(foundBill.getSubscriptionId(), acc.getIBAN());
            }

            JOptionPane.showMessageDialog(this, "Η πληρωμή ολοκληρώθηκε!");
            refreshData();
            if (dashboard != null) dashboard.refreshAllTabs();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Σφάλμα: " + ex.getMessage());
        }
    }

    private void searchForBill() {
        String code = codeField.getText().trim();
        if (code.isEmpty()) return;

        foundBill = BankSystem.getInstance().findBillByCode(code);

        if (foundBill == null) {
            detailsLabel.setText("<html><i>Ο κωδικός δεν βρέθηκε.</i></html>");
            payBtn.setEnabled(false);
            monthlyAutoPayCheck.setVisible(false);
            return;
        }

        boolean isAutoPayOn = false;
        if (foundBill.isMonthly()) {
            isAutoPayOn = BankSystem.getInstance().isSubscriptionAutoPayEnabled(foundBill.getSubscriptionId());
        }

        if (foundBill.isPaid()) {
            detailsLabel.setText("<html><b style='color:green;'>Πληρωμένος Λογαριασμός.</b><br>Μπορείτε να αλλάξετε την κατάσταση της συνδρομής:</html>");
            payBtn.setEnabled(false);
            if (foundBill.isMonthly()) {
                monthlyAutoPayCheck.setVisible(true);
                monthlyAutoPayCheck.setSelected(isAutoPayOn);
                monthlyAutoPayCheck.setText("Ενεργή πάγια εντολή για μελλοντικούς μήνες");
            }
        } else {
            detailsLabel.setText(String.format("<html><b>Επιχείρηση:</b> %s<br><b>Ποσό:</b> %.2f€</html>", 
                    foundBill.getBusinessName(), foundBill.getAmount()));
            payBtn.setEnabled(true);
            if (foundBill.isMonthly()) {
                monthlyAutoPayCheck.setVisible(true);
                monthlyAutoPayCheck.setSelected(isAutoPayOn);
                monthlyAutoPayCheck.setText("Ενεργοποίηση πάγιας εντολής (αυτόματη πληρωμή)");
            } else {
                monthlyAutoPayCheck.setVisible(false);
            }
        }
    }

    @Override public void refresh() { refreshData(); }

    private void refreshData() {
        if (!codeField.getText().trim().isEmpty()) searchForBill();
        revalidate(); repaint();
    }
}