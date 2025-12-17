package frontend.gui.tabs;

import javax.swing.*;
import java.awt.*;




public class AllTransactionsTab extends JPanel {

    public AllTransactionsTab() {
        setLayout(new BorderLayout());
        add(new JLabel("All Transactions — Admin/Auditor (placeholder)", SwingConstants.CENTER),
            BorderLayout.CENTER);
    }
}
