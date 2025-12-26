package frontend.gui;

import backend.BankSystem;
import backend.PasswordHasher;
import backend.users.User;
import javax.swing.*;
import java.awt.*;

public class ChangePasswordFrame extends JFrame {
    
    public ChangePasswordFrame() {
        User currentUser = UserSession.getInstance().getCurrentUser();
        
        setTitle("Αλλαγή Κωδικού Πρόσβασης");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel p = new JPanel(new GridLayout(3, 2, 10, 15));
        p.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPasswordField oldp = new JPasswordField();
        JPasswordField newp = new JPasswordField();
        JPasswordField conf = new JPasswordField();

        p.add(new JLabel("Παλιός κωδικός:")); p.add(oldp);
        p.add(new JLabel("Νέος κωδικός:")); p.add(newp);
        p.add(new JLabel("Επιβεβαίωση νέου:")); p.add(conf);

        JButton save = new JButton("Αποθήκευση Αλλαγών");
        save.setBackground(new Color(0, 102, 204));
        save.setForeground(Color.WHITE);
        save.setFont(new Font("SansSerif", Font.BOLD, 12));

        save.addActionListener(e -> {
            String oldPass = new String(oldp.getPassword());
            String newPass = new String(newp.getPassword());
            String confirmPass = new String(conf.getPassword());

            try {
                // 1. Έλεγχος αν τα πεδία είναι κενά
                if (oldPass.isEmpty() || newPass.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Παρακαλώ συμπληρώστε όλα τα πεδία.");
                    return;
                }

                // 2. Επαλήθευση παλιού κωδικού
                String hashedOld = PasswordHasher.hash(oldPass);
                if (!hashedOld.equals(currentUser.getPassword())) {
                    JOptionPane.showMessageDialog(this, "Ο παλιός κωδικός είναι λανθασμένος!", "Σφάλμα", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // 3. Έλεγχος αν οι νέοι κωδικοί ταιριάζουν
                if (!newPass.equals(confirmPass)) {
                    JOptionPane.showMessageDialog(this, "Οι νέοι κωδικοί δεν ταιριάζουν.", "Σφάλμα", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                // 4. Ενημέρωση και αποθήκευση
                currentUser.setPassword(PasswordHasher.hash(newPass));
                BankSystem.getInstance().saveAllData(); // Οριστική αποθήκευση στο JSON
                
                JOptionPane.showMessageDialog(this, "Ο κωδικός άλλαξε επιτυχώς!");
                dispose();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Σφάλμα κατά την αλλαγή κωδικού.");
                ex.printStackTrace();
            }
        });

        add(p, BorderLayout.CENTER);
        JPanel f = new JPanel();
        f.add(save);
        add(f, BorderLayout.SOUTH);
    }
}