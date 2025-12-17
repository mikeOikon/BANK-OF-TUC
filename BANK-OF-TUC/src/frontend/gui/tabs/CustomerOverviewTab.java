package frontend.gui.tabs;

import javax.swing.*;
import java.awt.*;

public class CustomerOverviewTab extends JPanel {

    public CustomerOverviewTab() {
        setLayout(new BorderLayout(20, 20));
        setBorder(BorderFactory.createEmptyBorder(20,20,20,20));

        JLabel title = new JLabel("Account Overview");
        title.setFont(new Font("SansSerif", Font.BOLD, 20));
        add(title, BorderLayout.NORTH);

        JTextArea info = new JTextArea(
                "Balance: €X,XXX.XX\n\n" +
                "Recent Transactions:\n" +
                "- Placeholder transaction 1\n" +
                "- Placeholder transaction 2\n" +
                "- Placeholder transaction 3"
        );
        info.setEditable(false);

        add(info, BorderLayout.CENTER);
    }
}
