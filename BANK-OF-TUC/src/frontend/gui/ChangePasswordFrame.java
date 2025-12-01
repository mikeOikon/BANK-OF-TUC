package frontend.gui;

import javax.swing.*;
import java.awt.*;

public class ChangePasswordFrame extends JFrame {

    public ChangePasswordFrame() {
        setTitle("Αλλαγή Κωδικού Πρόσβασης");
        setSize(500, 350);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        JPasswordField oldPass = new JPasswordField();
        JPasswordField newPass = new JPasswordField();
        JPasswordField confirmPass = new JPasswordField();

        panel.add(new JLabel("Παλιός Κωδικός:"));
        panel.add(oldPass);
        panel.add(new JLabel("Νέος Κωδικός:"));
        panel.add(newPass);
        panel.add(new JLabel("Επιβεβαίωση νέου:"));
        panel.add(confirmPass);

        JButton saveButton = new JButton("Αποθήκευση");
        saveButton.addActionListener(e -> JOptionPane.showMessageDialog(this, "Κωδικός Αλλάχθηκε!"));

        add(panel, BorderLayout.CENTER);
        add(saveButton, BorderLayout.SOUTH);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ChangePasswordFrame().setVisible(true));
    }
}
