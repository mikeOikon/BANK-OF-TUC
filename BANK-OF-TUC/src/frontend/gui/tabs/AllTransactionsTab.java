package frontend.gui.tabs;

import backend.BankSystem;
import backend.accounts.Account;
import backend.transactions.Transaction;
import backend.users.Customer;
import backend.users.User;
import backend.users.ΒusinessCustomer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class AllTransactionsTab extends JPanel {

    private JTable transactionsTable;
    private DefaultTableModel tableModel;
    private TableRowSorter<DefaultTableModel> sorter;
    
    // UI Στοιχεία Φίλτρων
    private JTextField searchField;
    private JTextField minAmountField;
    private JTextField startDateField; // Format: YYYY-MM-DD
    private JTextField endDateField;   // Format: YYYY-MM-DD

    public AllTransactionsTab() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // --- ΠΑΝΩ ΜΕΡΟΣ: ΦΙΛΤΡΑ & ΑΝΑΖΗΤΗΣΗ ---
        JPanel topPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        
        // Σειρά 1: Search Bar
        JPanel searchPanel = new JPanel(new BorderLayout(5, 5));
        searchPanel.add(new JLabel("Search (User ID / IBAN): "), BorderLayout.WEST);
        searchField = new JTextField();
        searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { applyFilters(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { applyFilters(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { applyFilters(); }
        });
        searchPanel.add(searchField, BorderLayout.CENTER);
        // Σειρά 2: Advanced Filters
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        minAmountField = new JTextField(6);
        startDateField = new JTextField(8);
        endDateField = new JTextField(8);
        
        filterPanel.add(new JLabel("Min Amount (€):"));
        filterPanel.add(minAmountField);
        filterPanel.add(new JLabel("From (YYYY-MM-DD):"));
        filterPanel.add(startDateField);
        filterPanel.add(new JLabel("To (YYYY-MM-DD):"));
        filterPanel.add(endDateField);

        JButton applyBtn = new JButton("Apply Filters");
        JButton clearBtn = new JButton("Clear All");
        
        applyBtn.addActionListener(e -> applyFilters());
        clearBtn.addActionListener(e -> clearFilters());
        
        filterPanel.add(applyBtn);
        filterPanel.add(clearBtn);

        topPanel.add(searchPanel);
        topPanel.add(filterPanel);
        add(topPanel, BorderLayout.NORTH);

        // --- ΚΕΝΤΡΟ: ΠΙΝΑΚΑΣ ---
        String[] columns = {"Timestamp", "User ID", "IBAN", "Type", "Amount (€)", "Description"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };
        
        transactionsTable = new JTable(tableModel);
        sorter = new TableRowSorter<>(tableModel);
        transactionsTable.setRowSorter(sorter);
        
        // Default ταξινόμηση: Timestamp (Στήλη 0) Φθίνουσα
        List<RowSorter.SortKey> sortKeys = new ArrayList<>();
        sortKeys.add(new RowSorter.SortKey(0, SortOrder.DESCENDING));
        sorter.setSortKeys(sortKeys);

        add(new JScrollPane(transactionsTable), BorderLayout.CENTER);

        refreshData();
    }

    /**
     * Συλλέγει όλες τις συναλλαγές από όλους τους χρήστες και τις βάζει στον πίνακα
     */
    public void refreshData() {
        tableModel.setRowCount(0);
        BankSystem bank = BankSystem.getInstance();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        List<User> allUsers = new ArrayList<>();
        if (bank.getCustomers() != null) allUsers.addAll(bank.getCustomers().values());
        if (bank.getBusinessCustomers() != null) allUsers.addAll(bank.getBusinessCustomers().values());

        for (User user : allUsers) {
            if (user instanceof Customer customer) {
                for (Account acc : customer.getAccounts()) {
                    for (Transaction t : acc.getTransactions()) {
                        tableModel.addRow(new Object[]{
                            t.getTimestamp().format(formatter),
                            customer.getUserID(),
                            acc.getIBAN(),
                            t.getType(),
                            String.format("%.2f", t.getAmount()),
                            t.toString()
                        });
                    }
                }
            } else if (user instanceof ΒusinessCustomer bCust) {
                for (Account acc : bCust.getAccounts()) {
                    for (Transaction t : acc.getTransactions()) {
                        tableModel.addRow(new Object[]{
                            t.getTimestamp().format(formatter),
                            bCust.getUserID(),
                            acc.getIBAN(),
                            t.getType(),
                            String.format("%.2f", t.getAmount()),
                            t.toString()
                        });
                    }
                }
            }
        }
    }

    /**
     * Εφαρμόζει συνδυαστικά φίλτρα (Search + Amount + Dates)
     */
    private void applyFilters() {
        List<RowFilter<Object, Object>> filters = new ArrayList<>();

        // 1. Dynamic Text Search (ID ή IBAN) - Στήλες 1 και 2
        String searchText = searchField.getText().trim();
        if (!searchText.isEmpty()) {
            // Χρησιμοποιούμε regexFilter για case-insensitive αναζήτηση στις στήλες 1 και 2
            filters.add(RowFilter.regexFilter("(?i)" + searchText, 1, 2));
        }

        // 2. Minimum Amount Filter (Εφαρμόζεται μόνο αν υπάρχει έγκυρος αριθμός)
        String minAmtStr = minAmountField.getText().trim();
        if (!minAmtStr.isEmpty()) {
            try {
                double minAmount = Double.parseDouble(minAmtStr.replace(",", "."));
                filters.add(new RowFilter<Object, Object>() {
                    @Override
                    public boolean include(Entry<?, ?> entry) {
                        // Η στήλη 4 είναι το Amount (€)
                        String amtValue = entry.getStringValue(4).replace(",", ".");
                        return Double.parseDouble(amtValue) >= minAmount;
                    }
                });
            } catch (NumberFormatException e) {
                // Δεν σταματάμε το φιλτράρισμα, απλά αγνοούμε το λάθος ποσό μέχρι να διορθωθεί
            }
        }

        // 3. Date Range Filter
        String startStr = startDateField.getText().trim();
        String endStr = endDateField.getText().trim();
        if (!startStr.isEmpty() || !endStr.isEmpty()) {
            filters.add(new RowFilter<Object, Object>() {
                @Override
                public boolean include(Entry<?, ?> entry) {
                    String dateStr = entry.getStringValue(0).substring(0, 10); // YYYY-MM-DD
                    boolean matches = true;
                    if (!startStr.isEmpty()) matches &= dateStr.compareTo(startStr) >= 0;
                    if (!endStr.isEmpty()) matches &= dateStr.compareTo(endStr) <= 0;
                    return matches;
                }
            });
        }

        // Εφαρμογή όλων των φίλτρων μαζί (AND logic)
        if (filters.isEmpty()) {
            sorter.setRowFilter(null);
        } else {
            sorter.setRowFilter(RowFilter.andFilter(filters));
        }
    }

    private void clearFilters() {
        searchField.setText("");
        minAmountField.setText("");
        startDateField.setText("");
        endDateField.setText("");
        sorter.setRowFilter(null);
    }
}