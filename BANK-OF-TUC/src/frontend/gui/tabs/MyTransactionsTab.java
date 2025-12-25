package frontend.gui.tabs;

import javax.swing.*;
import java.awt.*;

import backend.transactions.Transaction;
import backend.users.Customer;

import java.util.List;
import java.util.stream.Collectors;

public class MyTransactionsTab extends JPanel {

    private final Customer customer;
    private final JTextArea historyArea;

    public MyTransactionsTab(Customer customer) {
        this.customer = customer;

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Τίτλος
        JLabel title = new JLabel("Global Transaction History");
        title.setFont(new Font("SansSerif", Font.BOLD, 20));
        add(title, BorderLayout.NORTH);

        // Περιοχή κειμένου για τις συναλλαγές
        historyArea = new JTextArea();
        historyArea.setEditable(false);
        historyArea.setFont(new Font("Monospaced", Font.PLAIN, 13));

        // Προσθήκη Scroll Pane (Αυτό επιτρέπει το scrolling)
        JScrollPane scrollPane = new JScrollPane(historyArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        add(scrollPane, BorderLayout.CENTER);

        // Κουμπί Ανανέωσης
        JButton refreshBtn = new JButton("Refresh History");
        refreshBtn.addActionListener(e -> refresh());
        
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footer.add(refreshBtn);
        add(footer, BorderLayout.SOUTH);

        refresh();
    }

    /**
     * Μαζεύει όλες τις συναλλαγές από όλους τους λογαριασμούς,
     * τις ταξινομεί κατά ημερομηνία και τις εμφανίζει.
     */
    public void refresh() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%-15s | %-12s | %-10s | %s\n", "Date", "Amount", "Type", "Account IBAN"));
        sb.append("----------------------------------------------------------------------\n");

        // Συλλογή όλων των συναλλαγών από τη λίστα λογαριασμών του πελάτη
        List<Transaction> allTransactions = customer.getAccounts().stream()
                .flatMap(acc -> acc.getTransactions().stream())
                // Προαιρετικά: Ταξινόμηση ώστε οι πιο πρόσφατες να είναι πάνω
                // .sorted(Comparator.comparing(Transaction::getDate).reversed())
                .collect(Collectors.toList());

        if (allTransactions.isEmpty()) {
            sb.append("\nNo transactions found across your accounts.");
        } else {
            for (Transaction t : allTransactions) {
                sb.append(t.toString()).append("\n");
            }
        }

        historyArea.setText(sb.toString());
        // Επαναφορά του scroll στην αρχή (πάνω)
        historyArea.setCaretPosition(0);
    }
}