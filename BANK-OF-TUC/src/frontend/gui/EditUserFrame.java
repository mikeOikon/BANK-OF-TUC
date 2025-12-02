package frontend.gui;

import javax.swing.*;
import java.awt.*;

public class EditUserFrame extends JFrame {
    public EditUserFrame() {
        setTitle("Αλλαγή στοιχείων χρήστη");
        setSize(700,420);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel root = new JPanel(new BorderLayout(8,8));
        root.setBorder(BorderFactory.createEmptyBorder(12,12,12,12));
        add(root);

        JLabel head = new JLabel("Επεξεργασία στοιχείων", SwingConstants.CENTER); head.setFont(new Font("SansSerif", Font.BOLD,16));
        root.add(head, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridLayout(4,2,12,12));
        JTextField name = new JTextField(safe(UserSession.getInstance().getUsername()));
        JTextField email = new JTextField(safe(UserSession.getInstance().getEmail()));
        JTextField phone = new JTextField("6901234567");
        JTextField addr = new JTextField("Χανιά, Κρήτη");
        form.add(new JLabel("Όνομα:")); form.add(name);
        form.add(new JLabel("Email:")); form.add(email);
        form.add(new JLabel("Τηλέφωνο:")); form.add(phone);
        form.add(new JLabel("Διεύθυνση:")); form.add(addr);
        root.add(form, BorderLayout.CENTER);

        JButton save = new JButton("Αποθήκευση");
        save.setBackground(new Color(0,153,76)); save.setForeground(Color.WHITE);
        save.addActionListener(e -> {
            // TODO: send updates to backend
            UserSession.getInstance().setUsername(name.getText().trim());
            UserSession.getInstance().setEmail(email.getText().trim());
            JOptionPane.showMessageDialog(this,"Οι αλλαγές αποθηκεύτηκαν.");
            dispose();
        });
        JPanel f = new JPanel(); f.add(save); root.add(f, BorderLayout.SOUTH);
    }
    private String safe(String s){ return s==null? "" : s; }
    public static void main(String[] args){ SwingUtilities.invokeLater(() -> new EditUserFrame().setVisible(true)); }
}
