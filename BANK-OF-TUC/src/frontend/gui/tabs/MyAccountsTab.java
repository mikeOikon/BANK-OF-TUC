package frontend.gui.tabs;

import backend.accounts.Account;
import backend.users.Customer;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MyAccountsTab extends JPanel {

    private final Customer customer;
    private final JList<Account> accountList; // Προσβάσιμο σε όλη την κλάση
    private Account selectedAccount;
    private JButton viewOverviewButton;
    private CustomerOverviewTab overviewTab;

    public MyAccountsTab(Customer customer, CustomerOverviewTab overviewTab) {
        this.customer = customer;
        this.overviewTab = overviewTab;

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Δημιουργία της λίστας
        accountList = new JList<>();
        accountList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Αρχικό γέμισμα της λίστας
        updateListModel();
        
        add(new JScrollPane(accountList), BorderLayout.CENTER);

        accountList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                selectedAccount = accountList.getSelectedValue();
            }
        });

        viewOverviewButton = new JButton("View Overview & Details");
        viewOverviewButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        add(viewOverviewButton, BorderLayout.SOUTH);

        viewOverviewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedAccount == null) {
                    JOptionPane.showMessageDialog(MyAccountsTab.this,
                            "Please select an account from the list first.",
                            "No Selection",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }

                overviewTab.setSelectedAccount(selectedAccount);
                
                Container parent = MyAccountsTab.this.getParent();
                if (parent instanceof JTabbedPane) {
                    JTabbedPane parentTabs = (JTabbedPane) parent;
                    parentTabs.setSelectedComponent(overviewTab);
                }
            }
        });
    }

    // Η μέθοδος refresh που καλείται από το OverviewTab
    public void refresh() {
        updateListModel();
        revalidate();
        repaint();
    }

    // Βοηθητική μέθοδος για να ανανεώνει τα περιεχόμενα της JList
    private void updateListModel() {
        DefaultListModel<Account> model = new DefaultListModel<>();
        for (Account acc : customer.getAccounts()) {
            model.addElement(acc);
        }
        accountList.setModel(model);
    }
}