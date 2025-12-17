package frontend.gui.tabs;

import javax.swing.*;
import java.awt.*;


public class MyAccountsTab extends JPanel {

    public MyAccountsTab() {
        setLayout(new BorderLayout());
        add(new JLabel("My Accounts (placeholder)", SwingConstants.CENTER),
            BorderLayout.CENTER);
    }
}