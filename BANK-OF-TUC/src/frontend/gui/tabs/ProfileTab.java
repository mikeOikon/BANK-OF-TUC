package frontend.gui.tabs;

import backend.users.User;

import javax.swing.*;
import java.awt.*;

public class ProfileTab extends JPanel {

    private final JTextField emailField;
    private final JTextField phoneField;

    public ProfileTab(User user) {

        setLayout(new BorderLayout(20, 20));
        setBorder(BorderFactory.createEmptyBorder(20,20,20,20));

        JLabel title = new JLabel("User Profile");
        title.setFont(new Font("SansSerif", Font.BOLD, 20));
        add(title, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridLayout(0, 2, 10, 10));

        form.add(new JLabel("Username:"));
        form.add(new JLabel(user.getUsername()));

        form.add(new JLabel("Name:"));
        form.add(new JLabel(user.getName() + " " + user.getSurname()));

        form.add(new JLabel("AFM:"));
        form.add(new JLabel(user.getAFM()));

        form.add(new JLabel("Email:"));
        emailField = new JTextField(user.getEmail());
        form.add(emailField);

        form.add(new JLabel("Phone Number:"));
        phoneField = new JTextField(user.getPhoneNumber());
        form.add(phoneField);

        add(form, BorderLayout.CENTER);

        JButton saveBtn = new JButton("Save Changes");
        saveBtn.addActionListener(e -> {
            user.setEmail(emailField.getText());
            user.setPhoneNumber(phoneField.getText());
            JOptionPane.showMessageDialog(this,
                    "Profile updated (placeholder)");
        });

        JPanel footer = new JPanel();
        footer.add(saveBtn);
        add(footer, BorderLayout.SOUTH);
    }
}
