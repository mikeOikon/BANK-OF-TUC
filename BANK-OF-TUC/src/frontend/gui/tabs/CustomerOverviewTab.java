package frontend.gui.tabs;

import backend.accounts.Account;
import backend.transactions.Transaction;
import backend.users.Customer;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class CustomerOverviewTab extends JPanel {

    private final JLabel balanceLabel;
    private final JLabel typeLabel; // Νέο Label για τον τύπο λογαριασμού
    private final JTextArea transactionsArea;
    
    private Account account;
    private Customer customer;
    private Account selectedAccount;

    public CustomerOverviewTab(Customer customer) {
        this.customer = customer;
        this.account = customer.getPrimaryAccount();

        setLayout(new BorderLayout(20, 20));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Τίτλος
        JLabel title = new JLabel("Account Overview");
        title.setFont(new Font("SansSerif", Font.BOLD, 20));
        add(title, BorderLayout.NORTH);

        // Στοιχεία Λογαριασμού (Τύπος και Υπόλοιπο)
        typeLabel = new JLabel();
        typeLabel.setFont(new Font("SansSerif", Font.ITALIC, 14));
        typeLabel.setForeground(Color.DARK_GRAY);

        balanceLabel = new JLabel();
        balanceLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        balanceLabel.setForeground(new Color(0, 102, 0)); // Σκούρο πράσινο για το υπόλοιπο

        // Panel για την οργάνωση των πληροφοριών στο πάνω μέρος
        JPanel infoPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        infoPanel.add(typeLabel);
        infoPanel.add(balanceLabel);

        // Περιοχή Συναλλαγών
        transactionsArea = new JTextArea();
        transactionsArea.setEditable(false);
        transactionsArea.setFont(new Font("Monospaced", Font.PLAIN, 13));

        JPanel content = new JPanel(new BorderLayout(10, 10));
        content.add(infoPanel, BorderLayout.NORTH);
        content.add(new JScrollPane(transactionsArea), BorderLayout.CENTER);

        add(content, BorderLayout.CENTER);
        
        // Footer με κουμπί δημιουργίας
        JButton createAccountBtn = new JButton("Create New Account");
        createAccountBtn.addActionListener(e -> showCreateAccountDialog());

        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footer.add(createAccountBtn);
        add(footer, BorderLayout.SOUTH);

        // Έλεγχος αν υπάρχει λογαριασμός
        if (account == null) {
            typeLabel.setText("No account selected.");
            balanceLabel.setText("Balance: 0.00 €");
            transactionsArea.setText("Please create or select an account.");
            return;
        }

        refresh();
    }

    //---------------------------------------------------------
    // UPDATE METHODS
    //---------------------------------------------------------
    private void refresh() {
        if (account != null) {
            updateType();
            updateBalance();
            updateTransactions();
        }
    }

    private void updateType() {
        // Παίρνει το όνομα της κλάσης και προσθέτει κενά (π.χ. SavingsAccount -> Savings Account)
        String rawName = account.getClass().getSimpleName();
        String formattedName = rawName.replaceAll(
            String.format("%s|%s|%s",
                "(?<=[A-Z])(?=[A-Z][a-z])",
                "(?<=[^A-Z])(?=[A-Z])",
                "(?<=[A-Za-z])(?=[^A-Za-z])"
            ),
            " "
        );
        typeLabel.setText("Account Type: " + formattedName + " (" + account.getIBAN() + ")");
    }

    private void updateBalance() {
        balanceLabel.setText(
                String.format("Current Balance: %.2f €", account.getBalance())
        );
    }

    private void updateTransactions() {
        StringBuilder sb = new StringBuilder();
        sb.append("Recent Transactions:\n");
        sb.append("--------------------------------------------------\n\n");

        if (account.getTransactions().isEmpty()) {
            sb.append("No transactions found for this account.");
        } else {
            account.getTransactions().stream()
                    .limit(10)
                    .forEach(t -> sb.append("• ").append(t.toString()).append("\n"));
        }

        transactionsArea.setText(sb.toString());
        transactionsArea.setCaretPosition(0);
    }

    //---------------------------------------------------------
    // PUBLIC API
    //---------------------------------------------------------
    public void setSelectedAccount(Account account) {
        this.selectedAccount = account;
        updateDisplay();
    }
    
    private void updateDisplay() {
        if (selectedAccount != null) {
            this.account = selectedAccount;
            refresh();
        }
    }

    private void showCreateAccountDialog() {
        String[] options = {
                "Transactional Account",
                "Savings Account",
                "Fixed-Term Account"
        };

        int choice = JOptionPane.showOptionDialog(
                this,
                "Select the type of account you wish to open:",
                "Open New Account",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                options,
                options[0]
        );

        if (choice == -1) return;

        // Δημιουργία λογαριασμού (choice + 1 για να ταιριάζει με τη λογική 1,2,3)
        Account newAccount = customer.createAccount(choice + 1);

        if (this.account == null) {
            this.account = newAccount;
        }
        
        JOptionPane.showMessageDialog(
                this,
                "Success! New IBAN: " + newAccount.getIBAN(),
                "Account Created",
                JOptionPane.INFORMATION_MESSAGE
        );

        refresh();
    }
}