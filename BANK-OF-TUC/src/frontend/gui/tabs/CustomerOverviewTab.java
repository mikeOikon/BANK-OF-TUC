package frontend.gui.tabs;

import backend.ChartPanel;
import backend.accounts.Account;
import backend.transactions.Transaction;
import backend.users.User;
import backend.users.Customer;
import backend.users.BusinessCustomer;

import javax.swing.*;
import java.awt.*;
import java.util.Comparator;

public class CustomerOverviewTab extends JPanel {

    private final JLabel balanceLabel;
    private final JLabel typeLabel;
    private final JTextArea transactionsArea;
    private final JButton freezeBtn;
    
    private Account account;
    private final User customer; // Χρησιμοποιούμε το βασικό type User για ευελιξία
    
    private MyAccountsTab accountsTab;
    private MyTransactionsTab transactionsTab;
    private TransferTab transferTab;
    
    private ChartPanel chartPanel;

    public CustomerOverviewTab(User customer) {
        // ΔΙΟΡΘΩΣΗ: Αφαίρεση του λανθασμένου casting στον constructor
        this.customer = customer;
        this.account = customer.getPrimaryAccount();

        setLayout(new BorderLayout(20, 20));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // --- ΠΑΝΩ ΜΕΡΟΣ: Τίτλος και Freeze Button ---
        JPanel headerPanel = new JPanel(new BorderLayout());
        JLabel title = new JLabel("Account Overview");
        title.setFont(new Font("SansSerif", Font.BOLD, 20));
        
        freezeBtn = new JButton("Freeze Account");
        styleFreezeButton(freezeBtn);
        freezeBtn.addActionListener(e -> handleFreezeToggle());

        headerPanel.add(title, BorderLayout.WEST);
        headerPanel.add(freezeBtn, BorderLayout.EAST);
        add(headerPanel, BorderLayout.NORTH);

        // --- Στοιχεία Λογαριασμού ---
        typeLabel = new JLabel("Account Type: None");
        typeLabel.setFont(new Font("SansSerif", Font.ITALIC, 14));
        typeLabel.setForeground(Color.DARK_GRAY);

        balanceLabel = new JLabel("Current Balance: 0.00 €");
        balanceLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        balanceLabel.setForeground(new Color(0, 102, 0));

        JPanel infoPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        infoPanel.add(typeLabel);
        infoPanel.add(balanceLabel);

        // --- ΚΕΝΤΡΙΚΟ ΜΕΡΟΣ: Transactions & Chart ---
        transactionsArea = new JTextArea();
        transactionsArea.setEditable(false);
        transactionsArea.setFont(new Font("Monospaced", Font.PLAIN, 13));

        chartPanel = new ChartPanel();
        chartPanel.setPreferredSize(new Dimension(0, 180)); // Ελαφρώς μεγαλύτερο για ορατότητα
        chartPanel.setBackground(Color.WHITE);
        chartPanel.setBorder(BorderFactory.createTitledBorder("Monthly Cash Flow"));

        JPanel bottomContent = new JPanel(new BorderLayout());
        bottomContent.add(new JScrollPane(transactionsArea), BorderLayout.CENTER);
        bottomContent.add(chartPanel, BorderLayout.SOUTH);

        JPanel content = new JPanel(new BorderLayout(10, 10));
        content.add(infoPanel, BorderLayout.NORTH);
        content.add(bottomContent, BorderLayout.CENTER);

        add(content, BorderLayout.CENTER);
        
        // --- FOOTER ---
        JButton createAccountBtn = new JButton("Open New Account");
        createAccountBtn.addActionListener(e -> showCreateAccountDialog());

        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footer.add(createAccountBtn);
        add(footer, BorderLayout.SOUTH);

        refresh();
    }

    private void styleFreezeButton(JButton btn) {
        btn.setFocusPainted(false);
        btn.setFont(new Font("SansSerif", Font.BOLD, 12));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void handleFreezeToggle() {
        if (account == null) return;

        boolean isCurrentlyFrozen = account.isFrozen();
        String action = isCurrentlyFrozen ? "UNFREEZE" : "FREEZE";
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to " + action + " this account?\n" +
            "This will restrict all outgoing transactions.", 
            "Security Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            account.setFrozen(!isCurrentlyFrozen);
            backend.BankSystem.getInstance().saveAllData();
            refresh();
            JOptionPane.showMessageDialog(this, "Status updated: Account is now " + (account.isFrozen() ? "FROZEN" : "ACTIVE"));
        }
    }

    public void refresh() {
        if (account == null) {
            typeLabel.setText("No account selected.");
            balanceLabel.setText("Balance: 0.00 €");
            transactionsArea.setText("Please select an account.");
            freezeBtn.setEnabled(false);
        } else {
            freezeBtn.setEnabled(true);
            updateType();
            updateBalance();
            updateTransactions();
            chartPanel.setData(account.getTransactions());
            
            if (account.isFrozen()) {
                freezeBtn.setText("Unfreeze Account");
                freezeBtn.setBackground(new Color(255, 102, 102)); // Κόκκινο για ειδοποίηση
                freezeBtn.setForeground(Color.WHITE);
                balanceLabel.setForeground(Color.RED);
                balanceLabel.setText(String.format("Current Balance: %.2f € (FROZEN)", account.getBalance()));
            } else {
                freezeBtn.setText("Freeze Account");
                freezeBtn.setBackground(UIManager.getColor("Button.background"));
                freezeBtn.setForeground(Color.BLACK);
                balanceLabel.setForeground(new Color(0, 102, 0));
            }
        }
    }

    private void updateType() {
        String rawName = account.getClass().getSimpleName();
        String formattedName = rawName.replaceAll("(?<=[a-z])(?=[A-Z])", " ");
        typeLabel.setText("Account Type: " + formattedName + " | IBAN: " + account.getIBAN());
    }

    private void updateBalance() {
        if (!account.isFrozen()) {
            balanceLabel.setText(String.format("Current Balance: %.2f €", account.getBalance()));
        }
    }

    private void updateTransactions() {
        StringBuilder sb = new StringBuilder();
        sb.append("Recent Activity:\n");
        sb.append("--------------------------------------------------\n\n");

        if (account.getTransactions().isEmpty()) {
            sb.append("No recorded transactions.");
        } else {
            account.getTransactions().stream()
                    .sorted(Comparator.comparing(Transaction::getTimestamp).reversed())
                    .limit(10)
                    .forEach(t -> sb.append("• ").append(t.toString()).append("\n"));
        }
        transactionsArea.setText(sb.toString());
        transactionsArea.setCaretPosition(0);
    }

    public void setSelectedAccount(Account account) {
        this.account = account;
        refresh();
    }
    
    public void setOtherTabs(MyAccountsTab a, MyTransactionsTab t, TransferTab tr) {
        this.accountsTab = a;
        this.transactionsTab = t;
        this.transferTab = tr;
        if (this.account == null) {
            this.account = customer.getPrimaryAccount();
        }
        refresh();
    }

    private void showCreateAccountDialog() {
        String[] options = {"Transactional Account", "Savings Account", "Fixed-Term Account"};
        int choice = JOptionPane.showOptionDialog(this, "Select account type to open:", "New Account",
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

        if (choice == -1) return;

        // Κλήση της μεθόδου δημιουργίας (υποθέτοντας ότι η κλάση User έχει αυτή τη μέθοδο)
        Account newAccount = customer.createAccount(choice + 1);
        
        if (this.account == null) {
            this.account = newAccount;
        }
        
        backend.BankSystem.getInstance().saveAllData(); 
        JOptionPane.showMessageDialog(this, "Account created!\nIBAN: " + newAccount.getIBAN());
        refreshEntireSystem(); 
    }
    
    private void refreshEntireSystem() {
        refresh();
        // Αναζήτηση του JTabbedPane για καθολικό refresh
        Container parent = getParent();
        while (parent != null && !(parent instanceof JTabbedPane)) {
            parent = parent.getParent();
        }

        if (parent instanceof JTabbedPane tabs) {
            for (int i = 0; i < tabs.getTabCount(); i++) {
                Component tab = tabs.getComponentAt(i);
                // Πολυμορφικό refresh των tabs
                if (tab instanceof MyAccountsTab t) t.refresh();
                if (tab instanceof MyTransactionsTab t) t.refresh();
                if (tab instanceof TransferTab t) t.refresh();
                // Προσθήκη άλλων tabs αν χρειάζεται
            }
        }
    }
}