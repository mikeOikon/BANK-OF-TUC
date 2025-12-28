package frontend.gui.tabs;

import javax.swing.*;
import backend.users.User;
import frontend.gui.StartFrame;
import java.awt.*;
import services.Command;
import services.user_services.ChangePasswordCommand;
import backend.PasswordHasher; // Εισαγωγή του Hasher

public class SettingsTab extends JPanel {

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
            // 1. Ζητάμε το Παλιό Password
            JPasswordField oldPf = new JPasswordField();
            int resultOld = JOptionPane.showConfirmDialog(this, oldPf, "Enter Current Password:", JOptionPane.OK_CANCEL_OPTION);

            if (resultOld == JOptionPane.OK_OPTION) {
                String oldPassPlain = new String(oldPf.getPassword());

                // 2. Επαλήθευση Παλιού Password
                if (PasswordHasher.verify(oldPassPlain, currentUser.getPassword())) {
                    
                    // 3. Αν είναι σωστό, ζητάμε το Νέο Password
                    JPasswordField newPf = new JPasswordField();
                    int resultNew = JOptionPane.showConfirmDialog(this, newPf, "Enter New Password:", JOptionPane.OK_CANCEL_OPTION);

                    if (resultNew == JOptionPane.OK_OPTION) {
                        String newPassPlain = new String(newPf.getPassword());
                        
                        if (!newPassPlain.trim().isEmpty()) {
                            try {
                                // 4. Hashing του νέου password
                                String hashedNewPassword = PasswordHasher.hash(newPassPlain);
                                
                                // 5. Εκτέλεση του Command
                                Command changePass = new ChangePasswordCommand(currentUser, hashedNewPassword);
                                changePass.execute();
                                
                                JOptionPane.showMessageDialog(this, "Password changed successfully!");
                            } catch (Exception ex) {
                                JOptionPane.showMessageDialog(this, "Error while hashing.", "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        } else {
                            JOptionPane.showMessageDialog(this, "Password cannot be empty.");
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Wrong Password!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        JButton notifyBtn = new JButton("Προτιμήσεις Ειδοποιήσεων");
        JButton logoutBtn = new JButton("logout");
        logoutBtn.setForeground(new Color(180, 50, 50));
        logoutBtn.addActionListener(e -> handleLogout());

        optionsPanel.add(changePassBtn);
        optionsPanel.add(notifyBtn);
        optionsPanel.add(logoutBtn);

        add(optionsPanel, BorderLayout.CENTER);
    }

    private void handleLogout() {
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to logout?", 
            "logout", JOptionPane.YES_NO_OPTION);
            
        if (confirm == JOptionPane.YES_OPTION) {
            Window ancestor = SwingUtilities.getWindowAncestor(this);
            if (ancestor != null) {
                ancestor.dispose();
                new StartFrame().setVisible(true);
            }
        }
    }
}