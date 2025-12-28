package frontend.gui;

import backend.BankSystem;
import backend.PasswordHasher;
import backend.users.User;
import backend.users.UserBuilder;
import services.UserManager;
import types.UserType;
import user_services.CreateUserCommand;

import javax.swing.*;
import java.awt.*;

public class RegisterFrame extends JFrame {

    private JFrame loginFrame; // Αναφορά στο αρχικό παράθυρο login

    public RegisterFrame(JFrame loginFrame) {
        this.loginFrame = loginFrame; // Αποθήκευση της αναφοράς
        
        setTitle("Bank of TUC — Εγγραφή");
        setSize(820, 650); // Ελαφρώς μεγαλύτερο ύψος για άνεση
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

        // --- Πεδία Φόρμας ---
        
        // User type
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
        JLabel nameLabel = new JLabel("Όνομα:");
        form.add(nameLabel, c);
        c.gridx = 1; c.anchor = GridBagConstraints.WEST;
        JTextField nameField = new JTextField(20);
        form.add(nameField, c);

        // Surname
        c.gridx = 0; c.gridy++; c.anchor = GridBagConstraints.EAST;
        JLabel surnameLabel = new JLabel("Επίθετο:");
        form.add(surnameLabel, c);
        c.gridx = 1; c.anchor = GridBagConstraints.WEST;
        JTextField surnameField = new JTextField(20);
        form.add(surnameField, c);

        // AFM
        c.gridx = 0; c.gridy++; c.anchor = GridBagConstraints.EAST;
        form.add(new JLabel("ΑΦΜ:"), c);
        c.gridx = 1; c.anchor = GridBagConstraints.WEST;
        JTextField afmField = new JTextField(20);
        form.add(afmField, c);

        // Business fields
        c.gridx = 0; c.gridy++; c.anchor = GridBagConstraints.EAST;
        JLabel businessNameLabel = new JLabel("Όνομα Επιχείρησης:");
        form.add(businessNameLabel, c);
        c.gridx = 1; c.anchor = GridBagConstraints.WEST;
        JTextField businessNameField = new JTextField(20);
        form.add(businessNameField, c);

        c.gridx = 0; c.gridy++; c.anchor = GridBagConstraints.EAST;
        JLabel representativeLabel = new JLabel("Όνομα Εκπροσώπου:");
        form.add(representativeLabel, c);
        c.gridx = 1; c.anchor = GridBagConstraints.WEST;
        JTextField representativeField = new JTextField(20);
        form.add(representativeField, c);

        // Initially hide business fields
        businessNameLabel.setVisible(false);
        businessNameField.setVisible(false);
        representativeLabel.setVisible(false);
        representativeField.setVisible(false);

        userTypeBox.addActionListener(e -> {
            boolean business = userTypeBox.getSelectedIndex() == 1;

            // Personal fields
            nameField.setVisible(!business);
            surnameField.setVisible(!business);
            nameLabel.setVisible(!business);
            surnameLabel.setVisible(!business);

            // Business fields
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

        // --- CREATE ACTION ---
        createButton.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String rawPassword = new String(passwordField.getPassword());
            String rawConfirm = new String(confirmField.getPassword());
            String name = nameField.getText().trim();
            String surname = surnameField.getText().trim();
            String rawAfm = afmField.getText().trim();

            // 1. Validation
            if (username.isEmpty() || rawPassword.isEmpty() || rawConfirm.isEmpty()
                    || name.isEmpty() || surname.isEmpty() || rawAfm.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Συμπληρώστε όλα τα πεδία", "Σφάλμα", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (!rawPassword.equals(rawConfirm)) {
                JOptionPane.showMessageDialog(this, "Οι κωδικοί δεν ταιριάζουν", "Σφάλμα", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // 2. Hashing
            String hashedPassword;
            String hashedAfm;
            try {
                hashedPassword = PasswordHasher.hash(rawPassword);
                hashedAfm = PasswordHasher.hash(rawAfm);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Σφάλμα κατά την κρυπτογράφηση", "Σφάλμα", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // 3. User Setup
            UserType userType = (userTypeBox.getSelectedIndex() == 0) ? UserType.CUSTOMER : UserType.BUSINESSCUSTOMER;
            UserBuilder builder = new UserBuilder()
                    .withUsername(username)
                    .withPassword(hashedPassword)
                    .withName(name)
                    .withSurname(surname)
                    .withAFM(hashedAfm);

            if (userType == UserType.BUSINESSCUSTOMER) {
                String bName = businessNameField.getText().trim();
                String rName = representativeField.getText().trim();
                if (bName.isEmpty() || rName.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Συμπληρώστε τα στοιχεία επιχείρησης", "Σφάλμα", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                builder.withBusinessName(bName).withRepresentativeName(rName);
            }

            // 4. Save & Transition
            BankSystem bank = BankSystem.getInstance();
            if (bank.findUserByUsername(username) != null) {
                JOptionPane.showMessageDialog(this, "Το όνομα χρήστη υπάρχει ήδη!", "Σφάλμα", JOptionPane.ERROR_MESSAGE);
                return;
            }
            

            User user = bank.createUser(userType, builder);
            bank.dao.save(bank);

            UserSession.getInstance().setCurrentUser(user);
            JOptionPane.showMessageDialog(this, "Ο λογαριασμός δημιουργήθηκε με επιτυχία!", "Επιτυχία", JOptionPane.INFORMATION_MESSAGE);

            // ΚΛΕΙΣΙΜΟ ΠΑΡΑΘΥΡΩΝ
            if (this.loginFrame != null) {
                this.loginFrame.dispose(); // Κλείνει το LoginFrame
            }
            this.dispose(); // Κλείνει το RegisterFrame

            // Άνοιγμα Dashboard
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