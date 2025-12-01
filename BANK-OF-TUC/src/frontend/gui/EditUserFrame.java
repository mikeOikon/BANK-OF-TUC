package frontend.gui;

import javax.swing.*;
import java.awt.*;

public class EditUserFrame extends JFrame {

    public EditUserFrame() {
        setTitle("Αλλαγή στοιχείων χρήστη");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        JTextField nameField = new JTextField(UserSession.getInstance().getUsername());
        JTextField emailField = new JTextField(UserSession.getInstance().getEmail());
        JTextField phoneField = new JTextField();
        JTextField addressField = new JTextField();

        panel.add(new JLabel("Όνομα:"));
        panel.add(nameField);
        panel.add(new JLabel("Email:"));
        panel.add(emailField);
        panel.add(new JLabel("Τηλέφωνο:"));
        panel.add(phoneField);
        panel.add(new JLabel("Διεύθυνση:"));
        panel.add(addressField);

        JButton saveButton = new JButton("Αποθήκευση");
        saveButton.addActionListener(e -> JOptionPane.showMessageDialog(this, "Αποθηκεύτηκαν!"));

        add(panel, BorderLayout.CENTER);
        add(saveButton, BorderLayout.SOUTH);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new EditUserFrame().setVisible(true));
    }
}
