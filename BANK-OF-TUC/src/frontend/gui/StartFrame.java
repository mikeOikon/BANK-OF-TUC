package frontend.gui;

import backend.BankSystem;
import backend.FileLogger;
import backend.PasswordHasher;
import backend.users.User;

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

        // Load users from JSON
        BankSystem.dao.load();

        JPanel container = new JPanel(new GridLayout(1, 2));
        add(container);

        //---------------------------------------------------------
        // LEFT PANEL (LOGO)
        //---------------------------------------------------------
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBackground(new Color(0, 51, 102));
        container.add(leftPanel);

        ImageIcon logo = new ImageIcon(
                "src/frontend/gui/552644274_1308216287470008_1862680383229436246_n.jpg"
        );
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

        JLabel userLabel = new JLabel("Όνομα Χρήστη:");
        userLabel.setBounds(80, 160, 200, 30);
        userLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        rightPanel.add(userLabel);

        usernameField = new JTextField();
        usernameField.setBounds(290, 160, 200, 30);
        rightPanel.add(usernameField);

        JLabel passLabel = new JLabel("Κωδικός:");
        passLabel.setBounds(80, 230, 200, 30);
        passLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        rightPanel.add(passLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(290, 230, 200, 30);
        rightPanel.add(passwordField);

        JButton loginButton = new JButton("ΣΥΝΔΕΣΗ");
        loginButton.setBounds(80, 330, 180, 45);
        styleMainButton(loginButton);
        rightPanel.add(loginButton);

        JButton registerButton = new JButton("ΔΗΜΙΟΥΡΓΙΑ ΛΟΓΑΡΙΑΣΜΟΥ");
        registerButton.setBounds(290, 330, 200, 45);
        styleSecondaryButton(registerButton);
        rightPanel.add(registerButton);

        //---------------------------------------------------------
        // ACTIONS
        //---------------------------------------------------------
        loginButton.addActionListener(e -> handleLogin());
        registerButton.addActionListener(e -> new RegisterFrame(this).setVisible(true));
    }

    //---------------------------------------------------------
    // LOGIN LOGIC
    //---------------------------------------------------------
    private void handleLogin() {
    	FileLogger logger= FileLogger.getInstance();
        String username = usernameField.getText().trim();
        String rawPassword = new String(passwordField.getPassword());

        if (username.isEmpty() || rawPassword.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Παρακαλώ συμπληρώστε όλα τα πεδία.",
                    "Σφάλμα",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        BankSystem bank = BankSystem.getInstance();
        User user = bank.findUserByUsername(username);

        if (user == null || !user.login(username, rawPassword)) {
            JOptionPane.showMessageDialog(
                    this,
                    "Λάθος όνομα χρήστη ή κωδικός.",
                    "Αποτυχία σύνδεσης",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        // Successful login
        logger.log(types.LogLevel.INFO, types.LogCategory.USER, "[LOGIN] User "+username+" logged in.");
        UserSession.getInstance().setCurrentUser(user);

        // Open dashboard depending on role / behavior
        new DashboardFrame().setVisible(true);
        dispose();
    }

    //---------------------------------------------------------
    // BUTTON STYLES
    //---------------------------------------------------------
    private void styleMainButton(JButton btn) {
        btn.setBackground(new Color(0, 102, 204));
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("SansSerif", Font.BOLD, 16));
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    private void styleSecondaryButton(JButton btn) {
        btn.setBackground(new Color(220, 220, 220));
        btn.setForeground(Color.BLACK);
        btn.setFont(new Font("SansSerif", Font.BOLD, 14));
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    //---------------------------------------------------------
    // MAIN
    //---------------------------------------------------------
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new StartFrame().setVisible(true));
    }
}
