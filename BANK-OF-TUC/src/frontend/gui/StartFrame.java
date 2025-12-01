package frontend.gui;

import javax.swing.*;
import java.awt.*;

public class StartFrame extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;

    public StartFrame() {
        setTitle("Bank of TUC - Login");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel container = new JPanel(new GridLayout(1, 2));
        add(container);

        JPanel leftPanel = new JPanel(null);
        leftPanel.setBackground(new Color(0, 51, 102));
        JLabel title = new JLabel("BANK OF TUC");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Arial", Font.BOLD, 32));
        title.setBounds(150, 20, 350, 40);
        leftPanel.add(title);
        container.add(leftPanel);

        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(null);
        rightPanel.setBackground(Color.WHITE);

        JLabel userLabel = new JLabel("Όνομα Χρήστη:");
        userLabel.setBounds(50, 160, 200, 30);
        rightPanel.add(userLabel);
        usernameField = new JTextField();
        usernameField.setBounds(260, 160, 180, 30);
        rightPanel.add(usernameField);

        JLabel passLabel = new JLabel("Κωδικός:");
        passLabel.setBounds(50, 230, 200, 30);
        rightPanel.add(passLabel);
        passwordField = new JPasswordField();
        passwordField.setBounds(260, 230, 180, 30);
        rightPanel.add(passwordField);

        JButton loginButton = new JButton("ΣΥΝΔΕΣΗ");
        loginButton.setBounds(50, 330, 180, 40);
        rightPanel.add(loginButton);

        JButton registerButton = new JButton("ΔΗΜΙΟΥΡΓΙΑ ΛΟΓΑΡΙΑΣΜΟΥ");
        registerButton.setBounds(260, 330, 180, 40);
        rightPanel.add(registerButton);

        loginButton.addActionListener(e -> {
            UserSession.getInstance().setUsername(usernameField.getText());
            new DashboardFrame().setVisible(true);
            dispose();
        });

        registerButton.addActionListener(e -> new RegisterFrame().setVisible(true));

        container.add(rightPanel);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new StartFrame().setVisible(true));
    }
}
