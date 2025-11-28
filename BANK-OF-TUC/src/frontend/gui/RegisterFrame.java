package frontend.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL; // Import for resource loading

public class RegisterFrame extends JFrame {

    private JTextField usernameField;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JButton registerButton;
    private JButton cancelButton;

    public RegisterFrame() {
        setTitle("Εγγραφή Χρήστη - Bank of TUC");
       
        setSize(900, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        
       
        JPanel mainPanel = new JPanel(new BorderLayout(0, 0));
        mainPanel.setBackground(new Color(255, 255, 255));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        
        JPanel contentPanel = new JPanel(new GridLayout(1, 2, 30, 0));
        contentPanel.setBackground(new Color(255, 255, 255));
        //contentPanel.setBackground(new Color(0, 51, 102));

        // Αριστερή στήλη - Λογότυπο
        JPanel logoPanel = createLogoPanel();
        contentPanel.add(logoPanel);

        // Δεξιά στήλη - Φόρμα
        JPanel formContainerPanel = new JPanel(new BorderLayout());
        formContainerPanel.setBackground(new Color(0, 51, 102));
        
        JPanel headerPanel = createHeaderPanel();
        formContainerPanel.add(headerPanel, BorderLayout.NORTH);

        JPanel centerPanel = createCenterPanel();
        formContainerPanel.add(centerPanel, BorderLayout.CENTER);

        JPanel footerPanel = createFooterPanel();
        formContainerPanel.add(footerPanel, BorderLayout.SOUTH);

        contentPanel.add(formContainerPanel);
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        setContentPane(mainPanel);
    }

    private JPanel createLogoPanel() {
        JPanel logoPanel = new JPanel(new BorderLayout());
        logoPanel.setBackground(new Color(0, 51, 102));
        logoPanel.setBorder(BorderFactory.createEmptyBorder(50, 20, 50, 20));

        // Κύριο λογότυπο
        JLabel logoLabel = new JLabel("BANK OF TUC", SwingConstants.CENTER);
        logoLabel.setFont(new Font("Arial", Font.BOLD, 32));
        logoLabel.setForeground(Color.WHITE);
        logoLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        
        // --- ΑΛΛΑΓΗ 2: Ενσωμάτωση του λογότυπου από το αρχείο εικόνας ---
        JLabel imageLabel = new JLabel();
        try {
            // Load the image from the resources folder
            URL imageUrl = getClass().getResource("552644274_1308216287470008_1862680383229436246_n.jpg");

            if (imageUrl != null) {
                ImageIcon originalIcon = new ImageIcon(imageUrl);
                // Scale the image to a suitable size
                Image scaledImage = originalIcon.getImage().getScaledInstance(250, 350, Image.SCALE_SMOOTH);
                imageLabel.setIcon(new ImageIcon(scaledImage));
                imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
            } else {
                // Fallback text if image is not found
                imageLabel.setText("Το λογότυπο δεν βρέθηκε");
                imageLabel.setForeground(Color.RED);
            }
        } catch (Exception e) {
            e.printStackTrace();
            imageLabel.setText("Σφάλμα φόρτωσης λογότυπου");
            imageLabel.setForeground(Color.RED);
        }

        // Γραμμή διαχωρισμού
        JSeparator separator = new JSeparator(JSeparator.HORIZONTAL);
        separator.setBackground(new Color(200, 220, 255));
        separator.setForeground(new Color(200, 220, 255));
        separator.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        // Πληροφορίες ασφαλείας
        JLabel securityLabel = new JLabel("<html><div style='text-align: center; font-size: 11px; color: #cccccc;'>"
                + "Η δική σου τράπεζα ;)<br/>"
                + "</div></html>", SwingConstants.CENTER);
        securityLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        // Panel για το κάτω μέρος (separator και security info)
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setOpaque(false); // Make it transparent
        bottomPanel.add(separator, BorderLayout.NORTH);
        bottomPanel.add(securityLabel, BorderLayout.SOUTH);

        logoPanel.add(logoLabel, BorderLayout.NORTH);
        // Αντί για το sloganLabel, προσθέτουμε την εικόνα
        logoPanel.add(imageLabel, BorderLayout.CENTER);
        logoPanel.add(bottomPanel, BorderLayout.SOUTH);

        return logoPanel;
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(0, 51, 102));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        JLabel titleLabel = new JLabel("ΕΓΓΡΑΦΗ ΝΕΟΥ ΧΡΗΣΤΗ", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        JLabel subtitleLabel = new JLabel("Δημιουργία Νέου Λογαριασμού", SwingConstants.CENTER);
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        subtitleLabel.setForeground(new Color(200, 220, 255));

        headerPanel.add(titleLabel, BorderLayout.NORTH);
        headerPanel.add(subtitleLabel, BorderLayout.CENTER);

        return headerPanel;
    }

    private JPanel createCenterPanel() {
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(Color.white);
        centerPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 220, 255), 2),
                BorderFactory.createEmptyBorder(25, 25, 25, 25)
        ));

        // Panel για τα input fields με GridBagLayout
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 12, 12, 12);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        Color labelColor = new Color(0, 51, 102);

        // Username - Row 0
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.4;
        JLabel usernameLabel = new JLabel("Όνομα Χρήστη:");
        usernameLabel.setFont(new Font("Arial", Font.BOLD, 12));
        usernameLabel.setForeground(labelColor);
        formPanel.add(usernameLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 0.6;
        usernameField = createStyledField();
        formPanel.add(usernameField, gbc);

        // Email - Row 1
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.4;
        JLabel emailLabel = new JLabel("Διεύθυνση Email:");
        emailLabel.setFont(new Font("Arial", Font.BOLD, 12));
        emailLabel.setForeground(labelColor);
        formPanel.add(emailLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 0.6;
        emailField = createStyledField();
        formPanel.add(emailField, gbc);

        // Password - Row 2
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.4;
        JLabel passwordLabel = new JLabel("Κωδικός Πρόσβασης:");
        passwordLabel.setFont(new Font("Arial", Font.BOLD, 12));
        passwordLabel.setForeground(labelColor);
        formPanel.add(passwordLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.weightx = 0.6;
        passwordField = createStyledPasswordField();
        formPanel.add(passwordField, gbc);

        // Confirm Password - Row 3
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0.4;
        JLabel confirmLabel = new JLabel("Επιβεβαίωση Κωδικού:");
        confirmLabel.setFont(new Font("Arial", Font.BOLD, 12));
        confirmLabel.setForeground(labelColor);
        formPanel.add(confirmLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.weightx = 0.6;
        confirmPasswordField = createStyledPasswordField();
        formPanel.add(confirmPasswordField, gbc);

        centerPanel.add(formPanel, BorderLayout.CENTER);

        // Info panel
        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        infoPanel.setBackground(Color.WHITE);
        JLabel infoLabel = new JLabel("ℹ️ Όλα τα πεδία είναι υποχρεωτικά για την ασφάλεια του λογαριασμού σας");
        infoLabel.setFont(new Font("Arial", Font.PLAIN, 10));
        infoLabel.setForeground(Color.GRAY);
        infoPanel.add(infoLabel);
        infoPanel.setBorder(BorderFactory.createEmptyBorder(15, 10, 0, 10));

        centerPanel.add(infoPanel, BorderLayout.SOUTH);

        return centerPanel;
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

    private JPanel createFooterPanel() {
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        footerPanel.setBackground(new Color(0, 51, 102));
        footerPanel.setBorder(BorderFactory.createEmptyBorder(25, 0, 0, 0));

        // Register Button
        registerButton = new JButton("ΔΗΜΙΟΥΡΓΙΑ ΛΟΓΑΡΙΑΣΜΟΥ");
        registerButton.setFont(new Font("Arial", Font.BOLD, 14));
        registerButton.setForeground(Color.WHITE);
        registerButton.setBackground(new Color(0, 153, 76));
        registerButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0, 102, 51), 1),
                BorderFactory.createEmptyBorder(12, 30, 12, 30)
        ));
        registerButton.setFocusPainted(false);
        registerButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Cancel Button
        cancelButton = new JButton("ΑΚΥΡΩΣΗ");
        cancelButton.setFont(new Font("Arial", Font.BOLD, 14));
        cancelButton.setForeground(new Color(0, 51, 102));
        cancelButton.setBackground(Color.WHITE);
        cancelButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0, 51, 102), 1),
                BorderFactory.createEmptyBorder(12, 30, 12, 30)
        ));
        cancelButton.setFocusPainted(false);
        cancelButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Listeners
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleRegistration();
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        footerPanel.add(registerButton);
        footerPanel.add(cancelButton);

        return footerPanel;
    }

    private void handleRegistration() {
        String username = usernameField.getText().trim();
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword());
        String confirm = new String(confirmPasswordField.getPassword());

        // Βασικοί έλεγχοι
        if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirm.isEmpty()) {
            showErrorDialog("Συμπλήρωση Υποχρεωτικών Πεδίων",
                    "Παρακαλώ συμπληρώστε όλα τα απαιτούμενα πεδία για να συνεχίσετε.");
            return;
        }

        if (!password.equals(confirm)) {
            showErrorDialog("Σφάλμα Κωδικού",
                    "Οι κωδικοί πρόσβασης που εισάγατε δεν ταιριάζουν. Παρακαλώ ελέγξτε ξανά.");
            return;
        }

        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            showErrorDialog("Μη Έγκυρο Email",
                    "Η διεύθυνση email που εισάγατε δεν είναι έγκυρη. Παρακαλώ ελέγξτε τη μορφή της.");
            return;
        }

        if (username.matches("\\d+")) {
            showErrorDialog("Μη Έγκυρο Όνομα Χρήστη",
                    "Το όνομα χρήστη δεν μπορεί να αποτελείται αποκλειστικά από αριθμούς.");
            return;
        }

        if (password.length() < 6) {
            showErrorDialog("Αδύναμος Κωδικός",
                    "Ο κωδικός πρόσβασης πρέπει να αποτελείται από τουλάχιστον 6 χαρακτήρες.");
            return;
        }

        showSuccessDialog();
        dispose();
    }

    private void showErrorDialog(String title, String message) {
        JOptionPane.showMessageDialog(this,
                "<html><body><div style='width: 300px; padding: 10px;'>" +
                        "<h3 style='color: #cc0000; margin: 0 0 10px 0;'>" + title + "</h3>" +
                        "<p style='margin: 0;'>" + message + "</p>" +
                        "</div></body></html>",
                "Σφάλμα Συστήματος",
                JOptionPane.ERROR_MESSAGE);
    }

    private void showSuccessDialog() {
        JOptionPane.showMessageDialog(this,
                "<html><body><div style='width: 300px; padding: 10px; text-align: center;'>" +
                        "<h3 style='color: #006633; margin: 0 0 15px 0;'>Επιτυχής Δημιουργία Λογαριασμού!</h3>" +
                        "<p style='margin: 5px 0;'>Ο λογαριασμός σας δημιουργήθηκε με επιτυχία.</p>" +
                        "<p style='margin: 5px 0; font-size: 11px; color: #666;'>" +
                        "Θα λάβετε email επιβεβαίωσης στο εγγεγραμμένο email σας.</p>" +
                        "</div></body></html>",
                "Επιτυχία Εγγραφής",
                JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                RegisterFrame frame = new RegisterFrame();
                frame.setVisible(true);
            }
        });
    }
}