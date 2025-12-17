package frontend.gui.tabs;

import javax.swing.*;
import java.awt.*;



public class MyTransactionsTab extends JPanel {

    public MyTransactionsTab() {
        setLayout(new BorderLayout());
        add(new JLabel("My Transactions History (placeholder)", SwingConstants.CENTER),
            BorderLayout.CENTER);
    }
}