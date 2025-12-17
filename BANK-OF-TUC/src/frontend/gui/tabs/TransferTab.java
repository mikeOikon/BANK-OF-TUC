package frontend.gui.tabs;

import javax.swing.*;
import java.awt.*;

public class TransferTab extends JPanel {

    public TransferTab() {
        setLayout(new BorderLayout());
        add(new JLabel("Money Transfer (placeholder)", SwingConstants.CENTER),
            BorderLayout.CENTER);
    }
}