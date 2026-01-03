package frontend.gui.tabs;

import javax.swing.*;
import java.awt.*;

import backend.users.User;
import frontend.gui.DashboardFrame;
import backend.accounts.Account;
import backend.support.Bill;
import backend.BankSystem;
import services.account_services.PayBillCommand;
import services.account_services.TransactionCommand;
import types.TransactionType;

public class BillPaymentTab extends JPanel implements Refreshable {

    private JTextField codeField;
    private JLabel detailsLabel;
    private JButton payBtn;
    private JCheckBox monthlyAutoPayCheck;
    private JComboBox<Account> accountCombo;

    private final User currentUser;
    private Bill foundBill;
    private final DashboardFrame dashboard;

    public BillPaymentTab(User user, DashboardFrame dashboard) {
        this.currentUser = user;
        this.dashboard = dashboard;

        BankSystem bank = BankSystem.getInstance();
        
        // ❗ Αν δεν έχει κανέναν λογαριασμό
        if (user.getAccounts().isEmpty()) {
            setLayout(new BorderLayout());
            JLabel msg = new JLabel(
                    "<html><center>" +
                            "<h2>Δεν έχετε λογαριασμό</h2>" +
                            "<p>Ανοίξτε έναν λογαριασμό για να πληρώσετε λογαριασμούς.</p>" +
                            "</center></html>",
                    SwingConstants.CENTER
            );
            msg.setFont(new Font("SansSerif", Font.PLAIN, 18));
            add(msg, BorderLayout.CENTER);
            return;
        }

        setLayout(new BorderLayout(20, 20));
        setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        // ---------- HEADER ----------
        JLabel title = new JLabel("Ηλεκτρονική Πληρωμή & Πάγιες Εντολές");
        title.setFont(new Font("SansSerif", Font.BOLD, 22));
        add(title, BorderLayout.NORTH);

        // ---------- CENTER ----------
        JPanel center = new JPanel();
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));

        // RF code
        center.add(new JLabel("Κωδικός Πληρωμής (RF):"));
        codeField = new JTextField();
        codeField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        codeField.setFont(new Font("Monospaced", Font.BOLD, 16));
        center.add(codeField);

        JButton searchBtn = new JButton("Αναζήτηση Λογαριασμού");
        center.add(Box.createRigidArea(new Dimension(0, 8)));
        center.add(searchBtn);

        // Account selection
        center.add(Box.createRigidArea(new Dimension(0, 15)));
        center.add(new JLabel("Λογαριασμός Χρέωσης:"));

        accountCombo = new JComboBox<>(currentUser.getAccounts().toArray(new Account[0]));
        accountCombo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        accountCombo.setRenderer((list, value, index, isSelected, cellHasFocus) -> {
            JLabel l = new JLabel(value.getIBAN() + " | Υπόλοιπο: " + value.getBalance() + "€");
            l.setOpaque(true);
            if (isSelected) l.setBackground(new Color(230, 230, 230));
            return l;
        });
        center.add(accountCombo);

        // Bill details
        detailsLabel = new JLabel("<html><i>Εισάγετε κωδικό RF.</i></html>");
        detailsLabel.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        center.add(detailsLabel);

        // Monthly autopay
        monthlyAutoPayCheck = new JCheckBox("Ενεργοποίηση πάγιας εντολής");
        monthlyAutoPayCheck.setVisible(false);
        center.add(monthlyAutoPayCheck);

        // Pay button
        payBtn = new JButton("Πληρωμή");
        payBtn.setEnabled(false);
        payBtn.setBackground(new Color(46, 139, 87));
        payBtn.setForeground(Color.WHITE);
        center.add(Box.createRigidArea(new Dimension(0, 10)));
        center.add(payBtn);

        add(center, BorderLayout.CENTER);

        // ---------- ACTIONS ----------
        searchBtn.addActionListener(e -> searchForBill());
        payBtn.addActionListener(e -> handlePayment());

        // Αν αλλάξει account → επανέλεγξε αν μπορεί να ενεργοποιήσει subscription
        accountCombo.addActionListener(e -> updateSubscriptionCheckbox());
        monthlyAutoPayCheck.addActionListener(e -> toggleSubscription());
    }

    private void handlePayment() {
        if (foundBill == null) return;

        Account acc = (Account) accountCombo.getSelectedItem();
        if (acc == null || acc.isFrozen()) {
            JOptionPane.showMessageDialog(this, "Μη έγκυρος λογαριασμός.", "Σφάλμα", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (acc.getBalance() < foundBill.getAmount()) {
            JOptionPane.showMessageDialog(this, "Ανεπαρκές υπόλοιπο.", "Σφάλμα", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
        	 Account businessAccount =
                     BankSystem.getInstance().getAccountbyNumber(foundBill.getBusinessIBAN());

             if (businessAccount == null) {
                 throw new IllegalStateException("Ο λογαριασμός επιχείρησης δεν βρέθηκε.");
             }

             // ✅ ΣΩΣΤΟ COMMAND
             PayBillCommand cmd = new PayBillCommand(
                     acc,
                     foundBill
             );

            cmd.execute();
            updateSubscriptionCheckbox();
            JOptionPane.showMessageDialog(this, "Η πληρωμή ολοκληρώθηκε!");
            refresh();
            if (dashboard != null) dashboard.refreshAllTabs();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Σφάλμα: " + ex.getMessage(),
                    "Σφάλμα", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void searchForBill() {
        String code = codeField.getText().trim();
        if (code.isEmpty()) return;

        foundBill = BankSystem.getInstance().findBillByCode(code);

        if (foundBill == null) {
            detailsLabel.setText("<html><i>Ο λογαριασμός δεν βρέθηκε.</i></html>");
            payBtn.setEnabled(false);
            monthlyAutoPayCheck.setVisible(false);
            return;
        }

        if (foundBill.isPaid()) {
            detailsLabel.setText("<html><b style='color:green;'>Ο λογαριασμός έχει πληρωθεί.</b></html>");
            payBtn.setEnabled(false);
            monthlyAutoPayCheck.setVisible(false);
            return;
        }

        detailsLabel.setText(String.format(
                "<html><b>Επιχείρηση:</b> %s<br><b>Ποσό:</b> %.2f€</html>",
                foundBill.getBusinessName(),
                foundBill.getAmount()
        ));

        payBtn.setEnabled(true);
        updateSubscriptionCheckbox();
    }

    // Ελέγχει αν μπορεί να ενεργοποιηθεί/απενεργοποιηθεί η πάγια εντολή
    private void updateSubscriptionCheckbox() {
        if (foundBill == null || !foundBill.isMonthly()) {
            monthlyAutoPayCheck.setVisible(false);
            return;
        }

        monthlyAutoPayCheck.setVisible(true);
        Account selected = (Account) accountCombo.getSelectedItem();
        String activeIBAN = BankSystem.getInstance().getActiveAutoPayIBAN(foundBill.getSubscriptionId());

        if (activeIBAN == null) {
            // Δεν υπάρχει ενεργή πάγια → μπορεί να ενεργοποιήσει
            monthlyAutoPayCheck.setEnabled(true);
            monthlyAutoPayCheck.setSelected(false);
        } else if (activeIBAN.equals(selected.getIBAN())) {
            // Αυτή η πάγια ανήκει στον επιλεγμένο λογαριασμό → μπορεί να απενεργοποιήσει
            monthlyAutoPayCheck.setEnabled(true);
            monthlyAutoPayCheck.setSelected(true);
        } else {
            // Η πάγια ανήκει σε άλλο λογαριασμό → δεν μπορεί να αλλάξει
            monthlyAutoPayCheck.setEnabled(false);
            monthlyAutoPayCheck.setSelected(true);
        }
    }

    // Ενεργοποιεί ή απενεργοποιεί πάγια
    private void toggleSubscription() {
        if (foundBill == null || !foundBill.isMonthly()) return;
        Account selected = (Account) accountCombo.getSelectedItem();
        if (!monthlyAutoPayCheck.isEnabled()) return; // δεν επιτρέπεται

        if (monthlyAutoPayCheck.isSelected()) {
            BankSystem.getInstance().enableMonthlyAutoPay(foundBill.getSubscriptionId(), selected.getIBAN());
        } else {
            BankSystem.getInstance().disableMonthlyAutoPay(foundBill.getSubscriptionId());
        }
    }

    @Override
    public void refresh() {
        if (!currentUser.getAccounts().isEmpty() && !codeField.getText().trim().isEmpty()) {
            searchForBill();
        }
        revalidate();
        repaint();
    }
}
