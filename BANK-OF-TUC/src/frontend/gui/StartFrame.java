package frontend.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

public class StartFrame extends JFrame {
//nikk
    private JTextField userIdField;
    private JPasswordField passwordField;

    public StartFrame() {
        setTitle("Σύνδεση Χρήστη - Bank of TUC");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        Color mainBlue = new Color(0, 51, 102);
        Color lightBlue = new Color(200, 220, 255);

        // ===== ΚΥΡΙΟ PANEL =====
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        setContentPane(mainPanel);

        JPanel contentPanel = new JPanel(new GridLayout(1, 2, 0, 0));
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        // ===== ΑΡΙΣΤΕΡΗ ΣΤΗΛΗ - LOGO =====
        JPanel logoPanel = new JPanel(new BorderLayout());
        logoPanel.setBackground(mainBlue);
        logoPanel.setBorder(BorderFactory.createEmptyBorder(50, 20, 50, 20));

        JLabel logoLabel = new JLabel("BANK OF TUC", SwingConstants.CENTER);
        logoLabel.setFont(new Font("Arial", Font.BOLD, 32));
        logoLabel.setForeground(Color.WHITE);
        logoLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        JLabel imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        try {
            URL imageUrl = getClass().getResource("552644274_1308216287470008_1862680383229436246_n.jpg");
            if (imageUrl != null) {
                ImageIcon icon = new ImageIcon(imageUrl);
                Image scaled = icon.getImage().getScaledInstance(250, 350, Image.SCALE_SMOOTH);
                imageLabel.setIcon(new ImageIcon(scaled));
            } else {
                imageLabel.setText("Logo not found");
                imageLabel.setForeground(Color.RED);
            }
        } catch (Exception e) {
            imageLabel.setText("Σφάλμα φόρτωσης εικόνας");
            imageLabel.setForeground(Color.RED);
        }

        JLabel sloganLabel = new JLabel("<html><div style='text-align:center;font-size:11px;color:#cccccc;'>-----</div></html>", SwingConstants.CENTER);
        sloganLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        logoPanel.add(logoLabel, BorderLayout.NORTH);
        logoPanel.add(imageLabel, BorderLayout.CENTER);
        logoPanel.add(sloganLabel, BorderLayout.SOUTH);

        // ===== ΔΕΞΙΑ ΣΤΗΛΗ - LOGIN PANEL =====
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBackground(mainBlue);

        // Επικεφαλίδα
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(mainBlue);
        JLabel titleLabel = new JLabel("ΣΥΝΔΕΣΗ ΧΡΗΣΤΗ", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel, BorderLayout.NORTH);

        JLabel subtitleLabel = new JLabel("Πρόσβαση στον λογαριασμό σας", SwingConstants.CENTER);
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        subtitleLabel.setForeground(new Color(200, 220, 255));
        headerPanel.add(subtitleLabel, BorderLayout.SOUTH);
        rightPanel.add(headerPanel, BorderLayout.NORTH);

        // Κεντρικό μέρος - φόρμα
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(lightBlue, 2),
                BorderFactory.createEmptyBorder(25, 25, 25, 25)
        ));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 12, 12, 12);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        Color labelColor = new Color(0, 51, 102);

        // User ID
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel userLabel = new JLabel("Όνομα Χρήστη / User ID:");
        userLabel.setFont(new Font("Arial", Font.BOLD, 12));
        userLabel.setForeground(labelColor);
        formPanel.add(userLabel, gbc);

        gbc.gridx = 1;
        userIdField = createStyledField();
        formPanel.add(userIdField, gbc);

        // Password
        gbc.gridx = 0; gbc.gridy = 1;
        JLabel passLabel = new JLabel("Κωδικός Πρόσβασης:");
        passLabel.setFont(new Font("Arial", Font.BOLD, 12));
        passLabel.setForeground(labelColor);
        formPanel.add(passLabel, gbc);

        gbc.gridx = 1;
        passwordField = createStyledPasswordField();
        formPanel.add(passwordField, gbc);

        // ===== ΚΟΥΜΠΙΑ =====
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        buttonPanel.setBackground(mainBlue);

        JButton loginButton = new JButton("ΣΥΝΔΕΣΗ");
        loginButton.setFont(new Font("Arial", Font.BOLD, 14));
        loginButton.setForeground(Color.WHITE);
        loginButton.setBackground(new Color(0, 153, 76));
        loginButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0, 102, 51), 1),
                BorderFactory.createEmptyBorder(12, 30, 12, 30)
        ));
        loginButton.setFocusPainted(false);
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JButton registerButton = new JButton("ΔΗΜΙΟΥΡΓΙΑ ΛΟΓΑΡΙΑΣΜΟΥ");
        registerButton.setFont(new Font("Arial", Font.BOLD, 14));
        registerButton.setForeground(mainBlue);
        registerButton.setBackground(Color.WHITE);
        registerButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(mainBlue, 1),
                BorderFactory.createEmptyBorder(12, 30, 12, 30)
        ));
        registerButton.setFocusPainted(false);
        registerButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);

        // Προσθήκη στο δεξί πάνελ
        rightPanel.add(formPanel, BorderLayout.CENTER);
        rightPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Προσθήκη όλων στο contentPanel
        contentPanel.add(logoPanel);
        contentPanel.add(rightPanel);

        // ===== ACTIONS =====
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleLogin();
            }
        });

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                RegisterFrame reg = new RegisterFrame();
                reg.setVisible(true);
            }
        });
    }

    private JTextField createStyledField() {
        JTextField field = new JTextField();
        field.setFont(new Font("Arial", Font.PLAIN, 12));
        field.setBackground(Color.WHITE);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        field.setPreferredSize(new Dimension(250, 40));
        return field;
    }

    private JPasswordField createStyledPasswordField() {
        JPasswordField field = new JPasswordField();
        field.setFont(new Font("Arial", Font.PLAIN, 12));
        field.setBackground(Color.WHITE);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        field.setPreferredSize(new Dimension(250, 40));
        return field;
    }

    private void handleLogin() {
        String user = userIdField.getText().trim();
        String pass = new String(passwordField.getPassword());

        if (user.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Παρακαλώ συμπληρώστε όλα τα πεδία.",
                    "Σφάλμα", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (user.equals("user") && pass.equals("1234")) {
            JOptionPane.showMessageDialog(this, "Επιτυχής σύνδεση!", "Καλωσήρθατε", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Λανθασμένο User ID ή Κωδικός.", "Αποτυχία", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            StartFrame frame = new StartFrame();
            frame.setVisible(true);
        });
    }
}
