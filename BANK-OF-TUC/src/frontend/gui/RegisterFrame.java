package frontend.gui;

import javax.swing.*;
import java.awt.*;

public class RegisterFrame extends JFrame {

    public RegisterFrame() {
        setTitle("Εγγραφή Χρήστη - Bank of TUC");
        setSize(900, 650);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout());
        setContentPane(mainPanel);

        JLabel title = new JLabel("ΕΓΓΡΑΦΗ ΝΕΟΥ ΧΡΗΣΤΗ", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 22));
        mainPanel.add(title, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        JTextField usernameField = new JTextField();
        JTextField emailField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        JPasswordField confirmField = new JPasswordField();

        formPanel.add(new JLabel("Όνομα Χρήστη:"));
        formPanel.add(usernameField);
        formPanel.add(new JLabel("Email:"));
        formPanel.add(emailField);
        formPanel.add(new JLabel("Κωδικός:"));
        formPanel.add(passwordField);
        formPanel.add(new JLabel("Επιβεβαίωση Κωδικού:"));
        formPanel.add(confirmField);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        JButton registerButton = new JButton("ΔΗΜΙΟΥΡΓΙΑ ΛΟΓΑΡΙΑΣΜΟΥ");
        registerButton.addActionListener(e -> {
            UserSession.getInstance().setUsername(usernameField.getText());
            JOptionPane.showMessageDialog(this, "Λογαριασμός δημιουργήθηκε!");
            dispose();
        });
        mainPanel.add(registerButton, BorderLayout.SOUTH);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new RegisterFrame().setVisible(true));
    }
}
