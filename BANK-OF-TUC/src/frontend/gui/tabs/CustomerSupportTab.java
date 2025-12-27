package frontend.gui.tabs;

import backend.users.User;

import javax.swing.*;
import java.awt.*;

public class CustomerSupportTab extends JPanel {

    private final User employee;

    public CustomerSupportTab(User employee) {
        this.employee = employee;

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Title
        JLabel title = new JLabel("Customer Support");
        title.setFont(new Font("SansSerif", Font.BOLD, 18));
        add(title, BorderLayout.NORTH);

        // Placeholder content
        JTextArea placeholder = new JTextArea();
        placeholder.setEditable(false);
        placeholder.setLineWrap(true);
        placeholder.setWrapStyleWord(true);
        placeholder.setFont(new Font("SansSerif", Font.PLAIN, 14));
        placeholder.setText(
                "Customer Support Module\n\n" +
                "This section will allow bank employees to:\n" +
                "• View customer support requests\n" +
                "• Respond to tickets\n" +
                "• Track issue status\n\n" +
                "Status: UNDER DEVELOPMENT"
        );

        add(new JScrollPane(placeholder), BorderLayout.CENTER);

        // Bottom info bar
        JLabel footer = new JLabel(
                "Logged in as: " + employee.getUsername(),
                SwingConstants.RIGHT
        );
        footer.setFont(new Font("SansSerif", Font.ITALIC, 12));
        add(footer, BorderLayout.SOUTH);
    }
}
