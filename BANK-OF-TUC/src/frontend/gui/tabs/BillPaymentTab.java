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

        detailsLabel = new JLabel(
                "<html><i>Εισάγετε τον κωδικό πληρωμής για να δείτε τις λεπτομέρειες.</i></html>"
        );
        detailsLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        detailsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // ✅ Checkbox για μηνιαία πληρωμή
        monthlyAutoPayCheck = new JCheckBox("Ενεργοποίηση πάγιας εντολής (αυτόματη πληρωμή κάθε μήνα)");
        monthlyAutoPayCheck.setVisible(false);
        monthlyAutoPayCheck.setAlignmentX(Component.CENTER_ALIGNMENT);

        payBtn = new JButton("Επιβεβαίωση & Πληρωμή");
        payBtn.setEnabled(false);
        payBtn.setBackground(new Color(46, 139, 87));
        payBtn.setForeground(Color.WHITE);
        payBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        payBtn.setPreferredSize(new Dimension(220, 40));

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
    }

    private void handlePayment() {
        if (foundBill == null) return;

        Account acc = currentUser.getPrimaryAccount();
        if (acc == null || acc.isFrozen()) {
            JOptionPane.showMessageDialog(this,
                    "Δεν υπάρχει διαθέσιμος ενεργός λογαριασμός.",
                    "Σφάλμα", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (acc.getBalance() < foundBill.getAmount()) {
            JOptionPane.showMessageDialog(this,
                    "Ανεπαρκές υπόλοιπο.",
                    "Σφάλμα", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                String.format("Επιβεβαιώνετε την πληρωμή %.2f€;", foundBill.getAmount()),
                "Επιβεβαίωση",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm != JOptionPane.YES_OPTION) return;

        try {
            // Πληρωμή τρέχοντος bill
            new PayBillCommand(acc, foundBill).execute();

            // ✅ Αν είναι μηνιαίο και τσεκαρισμένο → πάγια εντολή
            if (foundBill.isMonthly() && monthlyAutoPayCheck.isSelected()) {
                BankSystem.getInstance()
                        .enableMonthlyAutoPay(foundBill.getSubscriptionId(), acc.getIBAN());
            }

            JOptionPane.showMessageDialog(this,
                    "Η πληρωμή ολοκληρώθηκε επιτυχώς!",
                    "Επιτυχία",
                    JOptionPane.INFORMATION_MESSAGE);

            refreshData();

            if (dashboard != null) {
                dashboard.refreshAllTabs();
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Σφάλμα: " + ex.getMessage(),
                    "Σφάλμα",
                    JOptionPane.ERROR_MESSAGE);
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

        if (foundBill.isPaid()) {
            detailsLabel.setText("<html><b style='color:red;'>Ο λογαριασμός έχει ήδη πληρωθεί.</b></html>");
            payBtn.setEnabled(false);
            monthlyAutoPayCheck.setVisible(false);
            return;
        }

        detailsLabel.setText(String.format(
                "<html><div style='background:white; padding:15px; border:1px solid #2e8b57;'>"
                        + "<b>Επιχείρηση:</b> %s<br>"
                        + "<b>Ποσό:</b> %.2f€<br>"
                        + "<b>Περιγραφή:</b> %s"
                        + "</div></html>",
                foundBill.getBusinessName(),
                foundBill.getAmount(),
                foundBill.getDescription()
        ));

        payBtn.setEnabled(true);

        // ✅ Εμφάνιση επιλογής μόνο για μηνιαία
        if (foundBill.isMonthly()) {
            monthlyAutoPayCheck.setVisible(true);
            monthlyAutoPayCheck.setSelected(foundBill.isAutoPayEnabled());
        } else {
            monthlyAutoPayCheck.setVisible(false);
        }
    }

    @Override
    public void refresh() {
        refreshData();
    }

    private void refreshData() {
        if (!codeField.getText().trim().isEmpty()) {
            searchForBill();
        } else {
            detailsLabel.setText("<html><i>Εισάγετε κωδικό RF.</i></html>");
            payBtn.setEnabled(false);
            monthlyAutoPayCheck.setVisible(false);
        }

        revalidate();
        repaint();
    }
}
