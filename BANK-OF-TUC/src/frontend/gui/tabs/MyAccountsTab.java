package frontend.gui.tabs;

import backend.BankSystem;
import backend.accounts.Account;
import backend.users.Customer;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;


public class MyAccountsTab extends JPanel {

    private final Customer customer;
    private final JList<Account> accountList;
    private Account selectedAccount;
    private JButton viewOverviewButton;
    private CustomerOverviewTab overviewTab; // Χρειαζόμαστε το reference

    // Πρόσθεσε το CustomerOverviewTab στον constructor
    public MyAccountsTab(Customer customer, CustomerOverviewTab overviewTab) {
        this.customer = customer;
        this.overviewTab = overviewTab; // Τώρα το MyAccountsTab ξέρει ποιο είναι το overview

        setLayout(new BorderLayout());

        // Λίστα λογαριασμών
        accountList = new JList<>(customer.getAccounts().toArray(new Account[0]));
        accountList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(accountList), BorderLayout.CENTER);

        accountList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                selectedAccount = accountList.getSelectedValue();
            }
        });

        viewOverviewButton = new JButton("View Overview");
        add(viewOverviewButton, BorderLayout.SOUTH);

        viewOverviewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedAccount == null) {
                    JOptionPane.showMessageDialog(MyAccountsTab.this,
                            "Please select an account first.",
                            "No account selected",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }

                // 1. Ενημέρωση του OverviewTab με το επιλεγμένο account
                overviewTab.setSelectedAccount(selectedAccount);
                
                // 2. Μεταφορά στο Overview tab
                // Υποθέτουμε ότι το MyAccountsTab είναι μέσα σε ένα JTabbedPane
                Container parent = MyAccountsTab.this.getParent();
                if (parent instanceof JTabbedPane) {
                    JTabbedPane parentTabs = (JTabbedPane) parent;
                    parentTabs.setSelectedComponent(overviewTab);
                }
            }
        });
    }
}
