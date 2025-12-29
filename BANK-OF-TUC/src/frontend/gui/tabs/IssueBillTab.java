package frontend.gui.tabs;

import javax.swing.*;
import java.awt.*;
import java.util.Random;
import backend.users.User;
import backend.support.Bill;
import services.account_services.CreateBillCommand;

public class IssueBillTab extends JPanel {
    private JTextField amountField;
    private JTextField descriptionField;
    private User businessUser;

    public IssueBillTab(User user) {
        this.businessUser = user;
        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // --- Τίτλος ---
        JLabel title = new JLabel("Issue New Bill / Invoice");
        title.setFont(new Font("SansSerif", Font.BOLD, 22));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        add(title, gbc);

        // --- Πεδίο Ποσού ---
        gbc.gridwidth = 1; gbc.gridy = 1;
        add(new JLabel("Bill Amount (€):"), gbc);
        amountField = new JTextField(15);
        gbc.gridx = 1;
        add(amountField, gbc);

        // --- Πεδίο Περιγραφής ---
        gbc.gridx = 0; gbc.gridy = 2;
        add(new JLabel("Description:"), gbc);
        descriptionField = new JTextField(15);
        gbc.gridx = 1;
        add(descriptionField, gbc);

        // --- Κουμπί Έκδοσης ---
        JButton issueBtn = new JButton("Generate Bill & Payment Code");
        issueBtn.setBackground(new Color(70, 130, 180));
        issueBtn.setForeground(Color.WHITE);
        issueBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        add(issueBtn, gbc);

        issueBtn.addActionListener(e -> handleIssueBill());
    }

    private void handleIssueBill() {
        try {
            double amount = Double.parseDouble(amountField.getText());
            String desc = descriptionField.getText().trim();
            
            if (amount <= 0 || desc.isEmpty()) {
                throw new Exception("Invalid input data.");
            }

            // 1. Παραγωγή τυχαίου κωδικού RF (π.χ. RF + 8 ψηφία)
            String paymentCode = "RF" + (10000000 + new Random().nextInt(90000000));

            // 2. Λήψη του IBAN της επιχείρησης (από τον primary λογαριασμό της)
            String businessIBAN = businessUser.getPrimaryAccount().getIBAN();
            
            String businessName = (String) businessUser.getFullName();

            // 3. Δημιουργία και Εκτέλεση του Command
            Bill newBill = new Bill(paymentCode, businessIBAN, businessName, amount, desc);
            new CreateBillCommand(newBill).execute();

            // 4. Ενημέρωση Χρήστη
            String message = String.format("Bill Issued Successfully!\nPayment Code: %s\nAmount: %.2f€", 
                                            paymentCode, amount);
            JOptionPane.showMessageDialog(this, message, "Success", JOptionPane.INFORMATION_MESSAGE);
            
            // Καθαρισμός πεδίων
            amountField.setText("");
            descriptionField.setText("");

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}