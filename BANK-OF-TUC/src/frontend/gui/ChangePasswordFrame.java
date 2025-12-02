package frontend.gui;

import javax.swing.*;
import java.awt.*;

public class ChangePasswordFrame extends JFrame {
    public ChangePasswordFrame() {
        setTitle("Αλλαγή κωδικού");
        setSize(520,340);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel p = new JPanel(new GridLayout(4,2,10,10));
        p.setBorder(BorderFactory.createEmptyBorder(18,18,18,18));
        JPasswordField oldp = new JPasswordField();
        JPasswordField newp = new JPasswordField();
        JPasswordField conf = new JPasswordField();
        p.add(new JLabel("Παλιός κωδικός:")); p.add(oldp);
        p.add(new JLabel("Νέος κωδικός:")); p.add(newp);
        p.add(new JLabel("Επιβεβαίωση νέου:")); p.add(conf);
        JButton save = new JButton("Αλλαγή");
        save.setBackground(new Color(0,102,204)); save.setForeground(Color.WHITE);
        save.addActionListener(e -> {
            if (!String.valueOf(newp.getPassword()).equals(String.valueOf(conf.getPassword()))) {
                JOptionPane.showMessageDialog(this,"Οι νέοι κωδικοί δεν ταιριάζουν","Σφάλμα",JOptionPane.WARNING_MESSAGE); return;
            }
            // TODO: verify old password with backend and update
            JOptionPane.showMessageDialog(this,"Ο κωδικός άλλαξε επιτυχώς");
            dispose();
        });
        add(p, BorderLayout.CENTER);
        JPanel f = new JPanel(); f.add(save); add(f, BorderLayout.SOUTH);
    }
    public static void main(String[] args){ SwingUtilities.invokeLater(() -> new ChangePasswordFrame().setVisible(true)); }
}
