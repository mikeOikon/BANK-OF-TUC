package frontend.gui;

import javax.swing.*;
import java.awt.*;

public class TransferFrame extends JFrame {
    public TransferFrame() {
        setTitle("Μεταφορά Χρημάτων");
        setSize(580,420);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel root = new JPanel(new BorderLayout(10,10));
        root.setBorder(BorderFactory.createEmptyBorder(14,14,14,14));
        add(root);

        JLabel head = new JLabel("Νέα Μεταφορά", SwingConstants.CENTER);
        head.setFont(new Font("SansSerif", Font.BOLD,16));
        root.add(head, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridLayout(4,2,12,12));
        form.add(new JLabel("Από λογαριασμό:"));
        form.add(new JComboBox<>(new String[]{"GR12... - Ταμιευτήριο", "GR98... - Τρεχ. "}));
        form.add(new JLabel("IBAN παραλήπτη:"));
        JTextField iban = new JTextField(); form.add(iban);
        form.add(new JLabel("Ποσό (€):")); JTextField amount = new JTextField(); form.add(amount);
        form.add(new JLabel("Σκοπός:")); JTextField purpose = new JTextField(); form.add(purpose);

        root.add(form, BorderLayout.CENTER);

        JButton send = new JButton("Επιβεβαίωση & Αποστολή");
        send.setBackground(new Color(0,102,204)); send.setForeground(Color.WHITE);
        send.addActionListener(e -> {
            // basic checks
            if (iban.getText().trim().isEmpty() || amount.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this,"Συμπληρώστε IBAN και ποσό","Σφάλμα", JOptionPane.WARNING_MESSAGE); return;
            }
            // TODO: Confirm OTP / call backend
            JOptionPane.showMessageDialog(this, "Η μεταφορά " + amount.getText() + " € προς " + iban.getText() + " εκτελέστηκε.");
            dispose();
        });
        JPanel foot = new JPanel(); foot.add(send);
        root.add(foot, BorderLayout.SOUTH);
    }

    public static void main(String[] args){ SwingUtilities.invokeLater(() -> new TransferFrame().setVisible(true)); }
}
