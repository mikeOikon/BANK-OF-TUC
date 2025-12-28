package frontend.gui.tabs;

import frontend.gui.UserSession;
import services.OpenTicketCommand;
import services.UserManager;

import javax.swing.*;

import backend.users.User;

import java.awt.*;

public class SupportTab extends JPanel {

    public SupportTab(User user) {
        setLayout(new BorderLayout(10, 10));

        JTextArea messageArea = new JTextArea(6, 40);
        JButton sendBtn = new JButton("Report a problem");

        add(new JScrollPane(messageArea), BorderLayout.CENTER);
        add(sendBtn, BorderLayout.SOUTH);

        sendBtn.addActionListener(e -> {
            String msg = messageArea.getText().trim();
            if (msg.isEmpty()) return;

            try {
                UserManager manager = new UserManager();
                manager.execute(
                        new OpenTicketCommand(
                                UserSession.getInstance().getCurrentUser(),
                                msg
                        )
                );

                JOptionPane.showMessageDialog(this, "Ticket created successfully!");
                messageArea.setText("");

            } catch (SecurityException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(),
                        "Access denied", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
