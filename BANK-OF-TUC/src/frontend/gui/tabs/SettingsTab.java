package frontend.gui.tabs;

import javax.swing.*;
import java.awt.*;

public class SettingsTab extends JPanel {

    public SettingsTab() {

        setLayout(new BorderLayout(20, 20));
        setBorder(BorderFactory.createEmptyBorder(20,20,20,20));

        JLabel title = new JLabel("Settings");
        title.setFont(new Font("SansSerif", Font.BOLD, 20));
        add(title, BorderLayout.NORTH);

        JPanel options = new JPanel(new GridLayout(0, 1, 10, 10));

        options.add(new JButton("Change Password (placeholder)"));
        options.add(new JButton("Notification Preferences (placeholder)"));
        options.add(new JButton("Language Settings (placeholder)"));
        options.add(new JButton("Logout (placeholder)"));

        add(options, BorderLayout.CENTER);
    }
}
