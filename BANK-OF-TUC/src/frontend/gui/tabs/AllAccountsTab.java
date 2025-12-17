package frontend.gui.tabs;

import javax.swing.*;
import java.awt.*;



public class AllAccountsTab extends JPanel {

    public AllAccountsTab() {
        setLayout(new BorderLayout());
        add(new JLabel("All Accounts — Admin/Auditor (placeholder)", SwingConstants.CENTER),
            BorderLayout.CENTER);
    }
}
