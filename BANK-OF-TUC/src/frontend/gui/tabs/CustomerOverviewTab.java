package frontend.gui.tabs;

import backend.ChartPanel;
import backend.accounts.Account;
import backend.transactions.Transaction;
import backend.users.Customer;

import javax.swing.*;
import java.awt.*;
import java.util.Comparator;

public class CustomerOverviewTab extends JPanel {

    private final JLabel balanceLabel;
    private final JLabel typeLabel;
    private final JTextArea transactionsArea;
    private final JButton freezeBtn; // Νέο κουμπί
    
    private Account account;
    private final Customer customer;
    
    private MyAccountsTab accountsTab;
    private MyTransactionsTab transactionsTab;
    private TransferTab transferTab;
    
    private ChartPanel chartPanel;

    public CustomerOverviewTab(Customer customer) {
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
        chartPanel.setPreferredSize(new Dimension(0, 150));
        chartPanel.setBackground(Color.WHITE);
        chartPanel.setBorder(BorderFactory.createTitledBorder("Monthly Income Flow"));

        JPanel bottomContent = new JPanel(new BorderLayout());
        bottomContent.add(new JScrollPane(transactionsArea), BorderLayout.CENTER);
        bottomContent.add(chartPanel, BorderLayout.SOUTH);

        JPanel content = new JPanel(new BorderLayout(10, 10));
        content.add(infoPanel, BorderLayout.NORTH);
        content.add(bottomContent, BorderLayout.CENTER);

        add(content, BorderLayout.CENTER);
        
        // --- FOOTER ---
        JButton createAccountBtn = new JButton("Create New Account");
        createAccountBtn.addActionListener(e -> showCreateAccountDialog());

        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footer.add(createAccountBtn);
        add(footer, BorderLayout.SOUTH);

        refresh();
    }

    private void styleFreezeButton(JButton btn) {
        btn.setFocusPainted(false);
        btn.setFont(new Font("SansSerif", Font.BOLD, 12));
    }

    private void handleFreezeToggle() {
        if (account == null) return;

        // Υποθέτουμε ότι η κλάση Account έχει μέθοδο isFrozen() και setFrozen(boolean)
        boolean isCurrentlyFrozen = account.isFrozen();
        String action = isCurrentlyFrozen ? "unfreeze" : "freeze";
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to " + action + " this account?", 
            "Confirm Action", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            account.setFrozen(!isCurrentlyFrozen);
            backend.BankSystem.getInstance().saveAllData();
            refresh();
            JOptionPane.showMessageDialog(this, "Account " + (isCurrentlyFrozen ? "Activated" : "Frozen") + " successfully.");
        }
    }

    public void refresh() {
        if (account == null) {
            typeLabel.setText("No account selected.");
            balanceLabel.setText("Balance: 0.00 €");
            transactionsArea.setText("Please create or select an account to view details.");
            freezeBtn.setEnabled(false);
        } else {
            freezeBtn.setEnabled(true);
            updateType();
            updateBalance();
            updateTransactions();
            chartPanel.setData(account.getTransactions());
            
            // Ενημέρωση εμφάνισης κουμπιού Freeze
            if (account.isFrozen()) {
                freezeBtn.setText("Unfreeze Account");
                freezeBtn.setBackground(new Color(255, 204, 0)); // Πορτοκαλί/Κίτρινο
                balanceLabel.setForeground(Color.RED);
                balanceLabel.setText(balanceLabel.getText() + " (FROZEN)");
            } else {
                freezeBtn.setText("Freeze Account");
                freezeBtn.setBackground(new Color(200, 200, 200)); // Γκρι
                balanceLabel.setForeground(new Color(0, 102, 0));
            }
        }

        if (accountsTab != null) accountsTab.refresh();
        if (transactionsTab != null) transactionsTab.refresh();
        if (transferTab != null) transferTab.refresh();
    }

    // Οι υπόλοιπες μέθοδοι (updateType, updateBalance, κλπ) παραμένουν ίδιες...

    private void updateType() {
        String rawName = account.getClass().getSimpleName();
        String formattedName = rawName.replaceAll("(?<=[a-z])(?=[A-Z])", " ");
        typeLabel.setText("Account Type: " + formattedName + " (" + account.getIBAN() + ")");
    }

    private void updateBalance() {
        balanceLabel.setText(String.format("Current Balance: %.2f €", account.getBalance()));
    }

    private void updateTransactions() {
        StringBuilder sb = new StringBuilder();
        sb.append("Recent Transactions:\n");
        sb.append("--------------------------------------------------\n\n");

        if (account.getTransactions().isEmpty()) {
            sb.append("No transactions found for this account.");
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
        int choice = JOptionPane.showOptionDialog(this, "Select Account Type:", "Open Account",
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);

        if (choice == -1) return;

        Account newAccount = customer.createAccount(choice + 1);
        
        if (this.account == null) {
            this.account = newAccount;
        }
        
        backend.BankSystem.getInstance().saveAllData(); 
        JOptionPane.showMessageDialog(this, "Success! IBAN: " + newAccount.getIBAN());
        refreshEntireSystem(); 
    }
    
    private void refreshEntireSystem() {
        refresh();
        Container parent = getParent();
        while (parent != null && !(parent instanceof JTabbedPane)) {
            parent = parent.getParent();
        }

        if (parent instanceof JTabbedPane tabs) {
            for (int i = 0; i < tabs.getTabCount(); i++) {
                Component tab = tabs.getComponentAt(i);
                if (tab instanceof AllAccountsTab) ((AllAccountsTab) tab).refresh();
                if (tab instanceof MyAccountsTab) ((MyAccountsTab) tab).refresh();
                if (tab instanceof MyTransactionsTab) ((MyTransactionsTab) tab).refresh();
                if (tab instanceof TransferTab) ((TransferTab) tab).refresh();
            }
        }
    }
}