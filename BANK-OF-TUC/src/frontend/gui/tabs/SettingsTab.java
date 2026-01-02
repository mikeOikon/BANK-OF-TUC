package frontend.gui.tabs;

import javax.swing.*;
import backend.users.User;
import frontend.gui.StartFrame;
import java.awt.*;
import services.Command;
import services.user_services.ChangePasswordCommand;
import backend.PasswordHasher;

public class SettingsTab extends JPanel implements Refreshable {

    public SettingsTab(User currentUser) {
        setLayout(new BorderLayout(20, 20));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("Account Settings");
        title.setFont(new Font("SansSerif", Font.BOLD, 22));
        add(title, BorderLayout.NORTH);

        JPanel optionsPanel = new JPanel(new GridLayout(0, 1, 10, 10));

        JButton changePassBtn = new JButton("Change Password");
        changePassBtn.setFont(new Font("SansSerif", Font.PLAIN, 14));

        changePassBtn.addActionListener(e -> {
            JPasswordField oldPf = new JPasswordField();
            int resultOld = JOptionPane.showConfirmDialog(
                    this, oldPf, "Enter Current Password:", JOptionPane.OK_CANCEL_OPTION
            );

            if (resultOld == JOptionPane.OK_OPTION) {
                String oldPassPlain = new String(oldPf.getPassword());

                if (PasswordHasher.verify(oldPassPlain, currentUser.getPassword())) {

                    JPasswordField newPf = new JPasswordField();
                    int resultNew = JOptionPane.showConfirmDialog(
                            this, newPf, "Enter New Password:", JOptionPane.OK_CANCEL_OPTION
                    );

                    if (resultNew == JOptionPane.OK_OPTION) {
                        String newPassPlain = new String(newPf.getPassword());

                        // Empty check
                        if (newPassPlain == null || newPassPlain.trim().isEmpty()) {
                            JOptionPane.showMessageDialog(this, "Password cannot be empty.", "Error", JOptionPane.WARNING_MESSAGE);
                            return;
                        }

                        // Code smell: double hashing of passwords should be avoided in real applications.
                        if (oldPassPlain.equals(newPassPlain)) {
                            JOptionPane.showMessageDialog(this,
                                    "Ο νέος κωδικός δεν μπορεί να είναι ίδιος με τον παλιό.",
                                    "Σφάλμα",
                                    JOptionPane.WARNING_MESSAGE);
                            return;
                        }

                        // Απλοί έλεγχοι για νέο κωδικό (μήκος, χαρακτήρες κλπ) μπορούν να προστεθούν εδώ
                        if (newPassPlain.length() < 6) {
                            JOptionPane.showMessageDialog(this,
                                    "Ο νέος κωδικός πρέπει να έχει τουλάχιστον 6 χαρακτήρες.",
                                    "Σφάλμα",
                                    JOptionPane.WARNING_MESSAGE);
                            return;
                        }

                        if (!newPassPlain.matches(".*[A-Z].*") || !newPassPlain.matches(".*[a-z].*") || !newPassPlain.matches(".*\\d.*")) {
                            JOptionPane.showMessageDialog(this,
                                    "Ο νέος κωδικός πρέπει να περιέχει τουλάχιστον ένα κεφαλαίο γράμμα, ένα πεζό γράμμα και έναν αριθμό.",
                                    "Σφάλμα",
                                    JOptionPane.WARNING_MESSAGE);
                            return;
                        }

                        try {
                            Command changePass = new ChangePasswordCommand(currentUser, newPassPlain);
                            changePass.execute();

                            JOptionPane.showMessageDialog(this, "Password changed successfully!");
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(this, "Error while hashing.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }

                } else {
                    JOptionPane.showMessageDialog(
                            this, "Wrong Password!", "Error", JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        });

        JButton notifyBtn = new JButton("Προτιμήσεις Ειδοποιήσεων");

        JButton logoutBtn = new JButton("LOGOUT");
        logoutBtn.setForeground(new Color(180, 50, 50));
        logoutBtn.addActionListener(e -> handleLogout());

        optionsPanel.add(changePassBtn);
        optionsPanel.add(notifyBtn);
        optionsPanel.add(logoutBtn);

        add(optionsPanel, BorderLayout.CENTER);
    }

    private void handleLogout() {
        int confirm = JOptionPane.showConfirmDialog(
                this, "Are you sure you want to logout?",
                "logout", JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            Window ancestor = SwingUtilities.getWindowAncestor(this);
            if (ancestor != null) {
                ancestor.dispose();
                new StartFrame().setVisible(true);
            }
        }
    }

    /**
     * ΥΠΟΧΡΕΩΤΙΚΟ για Refreshable
     */
    @Override
    public void refresh() {
        revalidate();
        repaint();
    }
}
