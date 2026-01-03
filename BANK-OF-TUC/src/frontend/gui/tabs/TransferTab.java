package frontend.gui.tabs;

import javax.swing.*;
import backend.BankSystem;
import backend.accounts.Account;
import backend.users.User;
import backend.users.Customer;
import services.Command;
import services.account_services.TransactionCommand;
import types.TransactionType;

import java.awt.*;

public class TransferTab extends JPanel implements Refreshable{

    private final User customer;
    private final CustomerOverviewTab overviewTab;

    private JComboBox<Account> accountSelector;
    private JComboBox<String> typeSelector;
    private JTextField amountField;
    private JTextField targetIbanField;
    private JLabel targetIbanLabel;
    private JButton executeBtn;

    public TransferTab(User user, CustomerOverviewTab overviewTab) {
        this.customer = user;
        this.overviewTab = overviewTab;

        setLayout(new BorderLayout(20, 20));
        setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        JLabel title = new JLabel("Execute Transaction", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 22));
        add(title, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridLayout(0, 1, 10, 10));

        form.add(new JLabel("Select Your Account:"));
        accountSelector = new JComboBox<>(user.getAccounts().toArray(new Account[0]));
        form.add(accountSelector);

        form.add(new JLabel("Transaction Type:"));
        String[] types = {"Withdrawal", "Deposit", "Transfer to IBAN"};
        typeSelector = new JComboBox<>(types);
        form.add(typeSelector);

        form.add(new JLabel("Amount (€):"));
        amountField = new JTextField();
        form.add(amountField);

        targetIbanLabel = new JLabel("Target IBAN:");
        targetIbanField = new JTextField();
        targetIbanLabel.setVisible(false);
        targetIbanField.setVisible(false);
        form.add(targetIbanLabel);
        form.add(targetIbanField);

        add(form, BorderLayout.CENTER);

        executeBtn = new JButton("Confirm Transaction");
        executeBtn.setFont(new Font("SansSerif", Font.BOLD, 16));
        executeBtn.setBackground(new Color(0, 102, 204));
        executeBtn.setForeground(Color.WHITE);
        add(executeBtn, BorderLayout.SOUTH);

        typeSelector.addActionListener(e -> {
            boolean isTransfer = typeSelector.getSelectedItem().equals("Transfer to IBAN");
            targetIbanLabel.setVisible(isTransfer);
            targetIbanField.setVisible(isTransfer);
            revalidate();
            repaint();
        });

        executeBtn.addActionListener(e -> handleTransaction());
    }

    private void handleTransaction() {
        Account selectedAcc = (Account) accountSelector.getSelectedItem();
        String type = (String) typeSelector.getSelectedItem();
        String amountStr = amountField.getText().trim();

        // 1. Βασικοί έλεγχοι εισαγωγής δεδομένων
        if (selectedAcc == null || amountStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.");
            return;
        }

        try {
            double amount = Double.parseDouble(amountStr);
            if (amount <= 0) {
                JOptionPane.showMessageDialog(this, "Amount must be positive.");
                return;
            }

            // 2. Έλεγχος Maturity για Fixed-Term Accounts
            // Ο έλεγχος γίνεται μόνο για συναλλαγές που αφαιρούν χρήματα (Withdrawal, Transfer)
            if (type.equals("Withdrawal") || type.equals("Transfer to IBAN")) {
                if (selectedAcc instanceof backend.accounts.FixedTermAccount fixedAcc) {
                    if (!fixedAcc.isMatured()) {
                        JOptionPane.showMessageDialog(this, 
                            "Transaction Denied: This Fixed-Term Account has not matured yet.\n" +
                            "Maturity Date: " + fixedAcc.getMaturityDate(), 
                            "Account Locked", 
                            JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                }
            }

            BankSystem bank = BankSystem.getInstance();
            Command command = null;
            String message = "";

            if (type.equals("Withdrawal")) {
                command = new TransactionCommand(TransactionType.WITHDRAW, selectedAcc, null, amount);
                message = "Withdrawal successful!";
            } else if (type.equals("Deposit")) {
                command = new TransactionCommand(TransactionType.DEPOSIT, null, selectedAcc, amount);
                message = "Deposit successful!";
            } else if (type.equals("Transfer to IBAN")) {
                String targetIban = targetIbanField.getText().trim();
                Account targetAcc = bank.getAccountbyNumber(targetIban);

                if (targetAcc == null) {
                    throw new IllegalArgumentException("Target IBAN not found in the system.");
                }
                if (targetAcc.getIBAN().equals(selectedAcc.getIBAN())) {
                    throw new IllegalArgumentException("Cannot transfer to the same account.");
                }

                command = new TransactionCommand(TransactionType.TRANSFER, selectedAcc, targetAcc, amount);
                message = "Transfer of " + amount + "€ successful!";
            }

            // 4. Εκτέλεση της συναλλαγής και ενημέρωση συστήματος
            if (command != null) {
                try {
                    command.execute();
                    bank.dao.save(bank); // Μόνιμη αποθήκευση αλλαγών

                    // Ενημέρωση του Overview Tab αν υπάρχει
                    if (overviewTab != null) {
                        overviewTab.setSelectedAccount(selectedAcc);
                    }

                    JOptionPane.showMessageDialog(this, message, "Success", JOptionPane.INFORMATION_MESSAGE);
                    
                    // Καθαρισμός πεδίων
                    amountField.setText("");
                    targetIbanField.setText("");
                    
                } catch (IllegalArgumentException | IllegalStateException ex) {
                    // Σφάλματα που αφορούν το balance ή το freeze status
                    JOptionPane.showMessageDialog(this, ex.getMessage(), "Transaction Failed", JOptionPane.ERROR_MESSAGE);
                }
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid numeric amount.", "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
            // Σφάλματα όπως το "Target IBAN not found"
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Transaction Failed", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void refresh() {
        accountSelector.removeAllItems();
        for (Account acc : customer.getAccounts()) {
            accountSelector.addItem(acc);
        }
    }
}
 