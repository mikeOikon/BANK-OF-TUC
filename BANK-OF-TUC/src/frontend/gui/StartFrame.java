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

        // Main container
        JPanel container = new JPanel(new GridLayout(1, 2));
        add(container);

        //---------------------------------------------------------
        // LEFT PANEL (Logo + Branding)
        //---------------------------------------------------------
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBackground(new Color(0, 51, 102)); // deep blue
        container.add(leftPanel);

        // 🔵 ΕΔΩ ΘΑ ΒΑΛΕΙΣ ΤΟ PATH ΤΗΣ ΦΩΤΟΓΡΑΦΙΑΣ ΣΟΥ
        // Παράδειγμα: ImageIcon logo = new ImageIcon("frontend/gui/banklogo.jpg");
        ImageIcon logo = new ImageIcon("src/frontend/gui/552644274_1308216287470008_1862680383229436246_n.jpg");

        // --------------------------------------------------------
        // ↑ βάλε εδώ το path της εικόνας που ανέβασες
        // --------------------------------------------------------

        // Scaling to fit nicely
        Image scaledImage = logo.getImage().getScaledInstance(350, 350, Image.SCALE_SMOOTH);
        JLabel logoLabel = new JLabel(new ImageIcon(scaledImage));
        logoLabel.setHorizontalAlignment(SwingConstants.CENTER);

        leftPanel.add(logoLabel, BorderLayout.CENTER);

        JLabel bankName = new JLabel("BANK OF TUC", SwingConstants.CENTER);
        bankName.setForeground(Color.WHITE);
        bankName.setFont(new Font("Arial", Font.BOLD, 34));
        bankName.setBorder(BorderFactory.createEmptyBorder(20, 0, 40, 0));
        leftPanel.add(bankName, BorderLayout.SOUTH);

        //---------------------------------------------------------
        // RIGHT PANEL (LOGIN FORM)
        //---------------------------------------------------------
        JPanel rightPanel = new JPanel(null);
        rightPanel.setBackground(Color.WHITE);
        container.add(rightPanel);

        JLabel loginTitle = new JLabel("ΣΥΝΔΕΣΗ ΧΡΗΣΤΗ");
        loginTitle.setFont(new Font("SansSerif", Font.BOLD, 24));
        loginTitle.setBounds(150, 60, 350, 40);
        rightPanel.add(loginTitle);

        // Username label
        JLabel userLabel = new JLabel("Όνομα Χρήστη:");
        userLabel.setBounds(80, 160, 200, 30);
        userLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        rightPanel.add(userLabel);

        usernameField = new JTextField();
        usernameField.setBounds(290, 160, 200, 30);
        rightPanel.add(usernameField);

        // Password label
        JLabel passLabel = new JLabel("Κωδικός:");
        passLabel.setBounds(80, 230, 200, 30);
        passLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        rightPanel.add(passLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(290, 230, 200, 30);
        rightPanel.add(passwordField);

        // Login button
        JButton loginButton = new JButton("ΣΥΝΔΕΣΗ");
        loginButton.setBounds(80, 330, 180, 45);
        styleMainButton(loginButton);
        rightPanel.add(loginButton);

        // Register button
        JButton registerButton = new JButton("ΔΗΜΙΟΥΡΓΙΑ ΛΟΓΑΡΙΑΣΜΟΥ");
        registerButton.setBounds(290, 330, 200, 45);
        styleSecondaryButton(registerButton);
        rightPanel.add(registerButton);

        //---------------------------------------------------------
        // Action Listeners
        //---------------------------------------------------------

        // Login logic
        loginButton.addActionListener(e -> {
            UserSession.getInstance().setUsername(usernameField.getText());
            new DashboardFrame().setVisible(true);
            dispose();
        });

        registerButton.addActionListener(e -> new RegisterFrame().setVisible(true));
    }

    // Main blue button style
    private void styleMainButton(JButton btn) {
        btn.setBackground(new Color(0, 102, 204));
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("SansSerif", Font.BOLD, 16));
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    // Secondary light button
    private void styleSecondaryButton(JButton btn) {
        btn.setBackground(new Color(220, 220, 220));
        btn.setForeground(Color.BLACK);
        btn.setFont(new Font("SansSerif", Font.BOLD, 14));
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new StartFrame().setVisible(true));
    }
}
