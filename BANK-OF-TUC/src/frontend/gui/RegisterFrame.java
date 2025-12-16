package frontend.gui;

import backend.BankSystem;
import backend.PasswordHasher;
import backend.users.User;
import backend.users.UserBuilder;
import backend.users.UserFactory;
import types.UserType;

import javax.swing.*;
import java.awt.*;

public class RegisterFrame extends JFrame {

    public RegisterFrame() {
        setTitle("Bank of TUC — Εγγραφή");
        setSize(820, 600);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        Color mainBlue = new Color(0, 51, 102);
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(Color.WHITE);
        setContentPane(root);

        JLabel header = new JLabel("ΔΗΜΙΟΥΡΓΙΑ ΛΟΓΑΡΙΑΣΜΟΥ", SwingConstants.CENTER);
        header.setFont(new Font("SansSerif", Font.BOLD, 18));
        header.setForeground(mainBlue);
        header.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        root.add(header, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Color.WHITE);
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(10, 10, 10, 10);

        // User type selection
        c.gridx = 0; c.gridy = 0; c.anchor = GridBagConstraints.EAST;
        form.add(new JLabel("Τύπος Λογαριασμού:"), c);
        c.gridx = 1; c.anchor = GridBagConstraints.WEST;
        String[] userTypes = {"Κανονικός Πελάτης", "Επιχειρηματικός Πελάτης"};
        JComboBox<String> userTypeBox = new JComboBox<>(userTypes);
        form.add(userTypeBox, c);

        // Username
        c.gridx = 0; c.gridy++; c.anchor = GridBagConstraints.EAST;
        form.add(new JLabel("Όνομα Χρήστη:"), c);
        c.gridx = 1; c.anchor = GridBagConstraints.WEST;
        JTextField usernameField = new JTextField(20);
        form.add(usernameField, c);

        // Password
        c.gridx = 0; c.gridy++; c.anchor = GridBagConstraints.EAST;
        form.add(new JLabel("Κωδικός:"), c);
        c.gridx = 1; c.anchor = GridBagConstraints.WEST;
        JPasswordField passwordField = new JPasswordField(20);
        form.add(passwordField, c);

        // Confirm password
        c.gridx = 0; c.gridy++; c.anchor = GridBagConstraints.EAST;
        form.add(new JLabel("Επιβεβαίωση:"), c);
        c.gridx = 1; c.anchor = GridBagConstraints.WEST;
        JPasswordField confirmField = new JPasswordField(20);
        form.add(confirmField, c);

        // Name
        c.gridx = 0; c.gridy++; c.anchor = GridBagConstraints.EAST;
        form.add(new JLabel("Όνομα:"), c);
        c.gridx = 1; c.anchor = GridBagConstraints.WEST;
        JTextField nameField = new JTextField(20);
        form.add(nameField, c);

        // Surname
        c.gridx = 0; c.gridy++; c.anchor = GridBagConstraints.EAST;
        form.add(new JLabel("Επίθετο:"), c);
        c.gridx = 1; c.anchor = GridBagConstraints.WEST;
        JTextField surnameField = new JTextField(20);
        form.add(surnameField, c);

        // AFM
        c.gridx = 0; c.gridy++; c.anchor = GridBagConstraints.EAST;
        form.add(new JLabel("ΑΦΜ:"), c);
        c.gridx = 1; c.anchor = GridBagConstraints.WEST;
        JTextField afmField = new JTextField(20);
        form.add(afmField, c);

        // Business fields (hidden for normal customers)
        c.gridx = 0; c.gridy++; c.anchor = GridBagConstraints.EAST;
        JLabel businessNameLabel = new JLabel("Όνομα Επιχείρησης:");
        c.gridx = 1; c.anchor = GridBagConstraints.WEST;
        JTextField businessNameField = new JTextField(20);
        form.add(businessNameLabel, c);
        form.add(businessNameField, c);

        c.gridx = 0; c.gridy++; c.anchor = GridBagConstraints.EAST;
        JLabel representativeLabel = new JLabel("Όνομα Εκπροσώπου:");
        c.gridx = 1; c.anchor = GridBagConstraints.WEST;
        JTextField representativeField = new JTextField(20);
        form.add(representativeLabel, c);
        form.add(representativeField, c);

        // Initially hide business fields
        businessNameLabel.setVisible(false);
        businessNameField.setVisible(false);
        representativeLabel.setVisible(false);
        representativeField.setVisible(false);

        // Show/hide business fields based on selection
        userTypeBox.addActionListener(e -> {
            boolean business = userTypeBox.getSelectedIndex() == 1;
            businessNameLabel.setVisible(business);
            businessNameField.setVisible(business);
            representativeLabel.setVisible(business);
            representativeField.setVisible(business);
            pack();
        });

        root.add(form, BorderLayout.CENTER);

        // Footer
        JPanel foot = new JPanel();
        JButton createButton = new JButton("Δημιουργία");
        stylePrimary(createButton);
        foot.add(createButton);
        root.add(foot, BorderLayout.SOUTH);

        // Create action
        createButton.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword());
            String confirm = new String(confirmField.getPassword());
            String name = nameField.getText().trim();
            String surname = surnameField.getText().trim();
            String afm = afmField.getText().trim();

            if (username.isEmpty() || password.isEmpty() || confirm.isEmpty()
                    || name.isEmpty() || surname.isEmpty() || afm.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Συμπληρώστε όλα τα πεδία", "Σφάλμα", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (!password.equals(confirm)) {
                JOptionPane.showMessageDialog(this, "Οι κωδικοί δεν ταιριάζουν", "Σφάλμα", JOptionPane.WARNING_MESSAGE);
                return;
            }

            UserType userType = (userTypeBox.getSelectedIndex() == 0) ? UserType.CUSTOMER : UserType.BUSINESSCUSTOMER;

            UserBuilder builder = new UserBuilder()
                    .withUsername(username)
                    .withPassword(password)
                    .withName(name)
                    .withSurname(surname)
                    .withAFM(afm);

            if (userType == UserType.BUSINESSCUSTOMER) {
                String businessName = businessNameField.getText().trim();
                String repName = representativeField.getText().trim();
                if (businessName.isEmpty() || repName.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Συμπληρώστε όλα τα πεδία της επιχείρησης", "Σφάλμα", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                builder.withBusinessName(businessName).withRepresentativeName(repName);
            }

            BankSystem bank = BankSystem.getInstance();
            if (bank.findUserByUsername(username) != null) {
                JOptionPane.showMessageDialog(this, "Το όνομα χρήστη υπάρχει ήδη!", "Σφάλμα", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String userID = bank.generateId(userType);
            User user = UserFactory.createUser(userType, userID, builder);
            bank.addUser(user);
            bank.saveAllData(); // save to JSON immediately

            UserSession.getInstance().setCurrentUser(user);
            JOptionPane.showMessageDialog(this, "Ο λογαριασμός δημιουργήθηκε με επιτυχία!", "Επιτυχία", JOptionPane.INFORMATION_MESSAGE);
            dispose();

            // Open dashboard for new user
            new DashboardFrame().setVisible(true);
        });

        pack();
    }

    private void stylePrimary(JButton b) {
        b.setBackground(new Color(0, 153, 76));
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setFont(new Font("SansSerif", Font.BOLD, 13));
    }
}

