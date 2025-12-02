package frontend.gui;

import javax.swing.*;
import java.awt.*;

public class RegisterFrame extends JFrame {
    public RegisterFrame() {
        setTitle("Bank of TUC — Εγγραφή");
        setSize(820,540);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        Color mainBlue = new Color(0,51,102);
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(Color.WHITE);
        setContentPane(root);

        JLabel header = new JLabel("ΔΗΜΙΟΥΡΓΙΑ ΛΟΓΑΡΙΑΣΜΟΥ", SwingConstants.CENTER);
        header.setFont(new Font("SansSerif", Font.BOLD, 18));
        header.setForeground(mainBlue);
        header.setBorder(BorderFactory.createEmptyBorder(16,16,16,16));
        root.add(header, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Color.WHITE);
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(10,10,10,10);
        c.gridx=0; c.gridy=0; c.anchor=GridBagConstraints.EAST;
        form.add(new JLabel("Όνομα χρήστη:"), c);
        c.gridx=1; c.anchor=GridBagConstraints.WEST;
        JTextField user = new JTextField(20); form.add(user,c);

        c.gridx=0;c.gridy++; c.anchor=GridBagConstraints.EAST; form.add(new JLabel("Email:"), c);
        c.gridx=1; c.anchor=GridBagConstraints.WEST; JTextField email = new JTextField(20); form.add(email,c);

        c.gridx=0;c.gridy++; c.anchor=GridBagConstraints.EAST; form.add(new JLabel("Κωδικός:"), c);
        c.gridx=1; c.anchor=GridBagConstraints.WEST; JPasswordField pass = new JPasswordField(20); form.add(pass,c);

        c.gridx=0;c.gridy++; c.anchor=GridBagConstraints.EAST; form.add(new JLabel("Επιβεβαίωση:"), c);
        c.gridx=1; c.anchor=GridBagConstraints.WEST; JPasswordField conf = new JPasswordField(20); form.add(conf,c);

        root.add(form, BorderLayout.CENTER);

        JPanel foot = new JPanel();
        JButton create = new JButton("Δημιουργία");
        stylePrimary(create);
        foot.add(create);
        root.add(foot, BorderLayout.SOUTH);

        create.addActionListener(e -> {
            // basic validation
            if (user.getText().trim().isEmpty() || email.getText().trim().isEmpty() ||
                pass.getPassword().length==0 || conf.getPassword().length==0) {
                JOptionPane.showMessageDialog(this,"Συμπληρώστε όλα τα πεδία","Σφάλμα", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (!String.valueOf(pass.getPassword()).equals(String.valueOf(conf.getPassword()))) {
                JOptionPane.showMessageDialog(this,"Οι κωδικοί δεν ταιριάζουν","Σφάλμα", JOptionPane.WARNING_MESSAGE);
                return;
            }
            // TODO: send registration to backend
            UserSession.getInstance().setUsername(user.getText().trim());
            UserSession.getInstance().setEmail(email.getText().trim());
            UserSession.getInstance().setUserId("u-"+user.getText().trim().hashCode());
            JOptionPane.showMessageDialog(this,"Ο λογαριασμός δημιουργήθηκε","OK", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        });
    }

    private void stylePrimary(JButton b) {
        b.setBackground(new Color(0,153,76)); b.setForeground(Color.WHITE);
        b.setFocusPainted(false); b.setFont(new Font("SansSerif", Font.BOLD, 13));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new RegisterFrame().setVisible(true));
    }
}
