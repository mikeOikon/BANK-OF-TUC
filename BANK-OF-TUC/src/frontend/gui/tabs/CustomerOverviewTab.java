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
    
    private Account account;
    private final Customer customer;
    
    private MyAccountsTab accountsTab;
    private MyTransactionsTab transactionsTab;
    private TransferTab transferTab;
    
    private ChartPanel chartPanel;
    
    double[] monthlyIncome = new double[12]; // Y axis data
    String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"}; // X axis

    public CustomerOverviewTab(Customer customer) {
        this.customer = customer;
        // Προσπάθεια ανάκτησης του πρώτου λογαριασμού (μπορεί να είναι null)
        this.account = customer.getPrimaryAccount();

        setLayout(new BorderLayout(20, 20));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Τίτλος
        JLabel title = new JLabel("Account Overview");
        title.setFont(new Font("SansSerif", Font.BOLD, 20));
        add(title, BorderLayout.NORTH);

        // Στοιχεία Λογαριασμού
        typeLabel = new JLabel("Account Type: None");
        typeLabel.setFont(new Font("SansSerif", Font.ITALIC, 14));
        typeLabel.setForeground(Color.DARK_GRAY);

        balanceLabel = new JLabel("Current Balance: 0.00 €");
        balanceLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        balanceLabel.setForeground(new Color(0, 102, 0));

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
        
        // Footer
        JButton createAccountBtn = new JButton("Create New Account");
        createAccountBtn.addActionListener(e -> showCreateAccountDialog());

        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footer.add(createAccountBtn);
        add(footer, BorderLayout.SOUTH);
        
     // Δημιουργία του Chart
        chartPanel = new ChartPanel();
        chartPanel.setPreferredSize(new Dimension(0, 150)); // Ύψος 200px
        chartPanel.setBackground(Color.WHITE);
        chartPanel.setBorder(BorderFactory.createTitledBorder("Monthly Income Flow"));

        // Προσθήκη στο UI
        // Θα χρειαστούμε ένα panel για να κρατάει το transactionsArea και το Chart μαζί
        JPanel bottomContent = new JPanel(new BorderLayout());
        bottomContent.add(new JScrollPane(transactionsArea), BorderLayout.CENTER);
        bottomContent.add(chartPanel, BorderLayout.SOUTH);

        content.add(bottomContent, BorderLayout.CENTER);

        refresh();
    }

    //---------------------------------------------------------
    // UPDATE METHODS (Refresh)
    //---------------------------------------------------------
    public void refresh() {
        // 1. Έλεγχος αν υπάρχει λογαριασμός (για αποφυγή NullPointerException)
        if (account == null) {
            typeLabel.setText("No account selected.");
            balanceLabel.setText("Balance: 0.00 €");
            transactionsArea.setText("Please create or select an account to view details.");
        } else {
            updateType();
            updateBalance();
            updateTransactions();
            chartPanel.setData(account.getTransactions());
        }

        // 2. Ενημέρωση των υπόλοιπων Tabs αν έχουν συνδεθεί
        if (accountsTab != null) accountsTab.refresh();
        if (transactionsTab != null) transactionsTab.refresh();
        if (transferTab != null) transferTab.refresh();
    }

    private void updateType() {
        String rawName = account.getClass().getSimpleName();
        // Προσθήκη κενών ανάμεσα σε κεφαλαία γράμματα
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
            // Ταξινόμηση: Πρόσφατα πάνω, limit 10
            account.getTransactions().stream()
                    .sorted(Comparator.comparing(Transaction::getTimestamp).reversed())
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
        this.account = account;
        refresh();
    }
    
    public void setOtherTabs(MyAccountsTab a, MyTransactionsTab t, TransferTab tr) {
        this.accountsTab = a;
        this.transactionsTab = t;
        this.transferTab = tr;
        // Αν μετά το login βρέθηκε λογαριασμός, τον ορίζουμε
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

        // ΑΝΤΙ ΓΙΑ refresh(), ΚΑΛΟΥΜΕ refreshEntireSystem()
        refreshEntireSystem(); 
    }
    
    private void refreshEntireSystem() {
        // 1. Refresh το ίδιο το tab
        refresh();

        // 2. Refresh όλων των άλλων tabs στο JTabbedPane (συμπεριλαμβανομένου του All Accounts του Admin)
        Container parent = getParent();
        while (parent != null && !(parent instanceof JTabbedPane)) {
            parent = parent.getParent();
        }

        if (parent instanceof JTabbedPane tabs) {
            for (int i = 0; i < tabs.getTabCount(); i++) {
                Component tab = tabs.getComponentAt(i);
                
                // Αν το tab είναι το AllAccountsTab του Admin, το κάνουμε refresh
                if (tab instanceof AllAccountsTab) ((AllAccountsTab) tab).refresh();
                
                // Refresh στα υπόλοιπα Customer Tabs (αν δεν έχουν ήδη γίνει από τη refresh())
                if (tab instanceof MyAccountsTab) ((MyAccountsTab) tab).refresh();
                if (tab instanceof MyTransactionsTab) ((MyTransactionsTab) tab).refresh();
                if (tab instanceof TransferTab) ((TransferTab) tab).refresh();
            }
        }
    }
    
    
}