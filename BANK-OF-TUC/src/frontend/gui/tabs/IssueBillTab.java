package frontend.gui.tabs;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

import backend.users.BusinessCustomer;
import backend.users.User;
import backend.support.Bill;
import backend.support.MonthlySubscription;
import backend.BankSystem; 
import services.account_services.CreateBillCommand;

public class IssueBillTab extends JPanel implements Refreshable {

    private JTextField amountField;
    private JTextField descriptionField;
    private JCheckBox monthlyCheck; // Checkbox για επιλογή συνδρομής
    private BusinessCustomer businessUser;

    public IssueBillTab(User user) {
        // Ασφαλής μετατροπή σε BusinessCustomer
        this.businessUser = (BusinessCustomer) user;
        
        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // --- Τίτλος ---
        JLabel title = new JLabel("Issue New Bill / Monthly Subscription");
        title.setFont(new Font("SansSerif", Font.BOLD, 22));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        add(title, gbc);

        // --- Πεδίο Ποσού ---
        gbc.gridwidth = 1; gbc.gridy = 1; gbc.gridx = 0;
        add(new JLabel("Amount (€):"), gbc);
        amountField = new JTextField(15);
        gbc.gridx = 1;
        add(amountField, gbc);

        // --- Πεδίο Περιγραφής ---
        gbc.gridx = 0; gbc.gridy = 2;
        add(new JLabel("Description:"), gbc);
        descriptionField = new JTextField(15);
        gbc.gridx = 1;
        add(descriptionField, gbc);

        // --- Επιλογή Μηνιαίας Συνδρομής ---
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        monthlyCheck = new JCheckBox("Create as Recurring Monthly Subscription");
        monthlyCheck.setToolTipText("If checked, a new bill will be generated automatically every month.");
        add(monthlyCheck, gbc);

        // --- Κουμπί Έκδοσης ---
        JButton issueBtn = new JButton("Generate Bill");
        issueBtn.setBackground(new Color(70, 130, 180));
        issueBtn.setForeground(Color.WHITE);
        issueBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        add(issueBtn, gbc);

        issueBtn.addActionListener(e -> handleIssueBill());
    }

    private void handleIssueBill() {
        try {
            // Έλεγχος αν ο χρήστης έχει λογαριασμό
            if (businessUser.getPrimaryAccount() == null) {
                throw new Exception("Business account not found.");
            }

            double amount = Double.parseDouble(amountField.getText());
            String desc = descriptionField.getText().trim();
            String businessIBAN = businessUser.getPrimaryAccount().getIBAN();
            String businessName = (String)businessUser.getFullName();

            if (amount <= 0 || desc.isEmpty()) throw new Exception("Invalid input data.");

            if (monthlyCheck.isSelected()) {
                // --- ΛΟΓΙΚΗ ΣΥΝΔΡΟΜΗΣ (SUBSCRIPTION) ---
                // Δημιουργούμε τη συνδρομή ΧΩΡΙΣ IBAN πελάτη (θα προστεθεί κατά την πληρωμή)
                MonthlySubscription sub = new MonthlySubscription(businessIBAN, businessName, amount, desc);
                
                // Προσθήκη στο κεντρικό σύστημα
                BankSystem.getInstance().addSubscription(sub);
                
                // Έκδοση του πρώτου Bill για τον τρέχοντα μήνα
                sub.generateMonthlyBill(BankSystem.getInstance());
                
                JOptionPane.showMessageDialog(this, 
                    "Monthly Subscription created successfully!\n" +
                    "The first bill has been issued. The customer can enable Auto-Pay when they pay.", 
                    "Subscription Created", JOptionPane.INFORMATION_MESSAGE);
            } else {
                // --- ΛΟΓΙΚΗ ΑΠΛΟΥ ΛΟΓΑΡΙΑΣΜΟΥ (ONE-TIME BILL) ---
                String paymentCode = "RF" + (10000000 + new Random().nextInt(90000000));
                Bill newBill = new Bill(paymentCode, businessIBAN, businessName, amount, desc);
                
                new CreateBillCommand(newBill).execute();

                JOptionPane.showMessageDialog(this, 
                    "One-time Bill Issued!\nPayment Code: " + paymentCode, 
                    "Success", JOptionPane.INFORMATION_MESSAGE);
            }

            refresh();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid amount.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void refresh() {
        amountField.setText("");
        descriptionField.setText("");
        monthlyCheck.setSelected(false);
        revalidate();
        repaint();
    }
}