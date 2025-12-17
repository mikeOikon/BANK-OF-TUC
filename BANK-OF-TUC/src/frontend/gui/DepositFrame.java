package frontend.gui;

import javax.swing.*;
import java.awt.*;

public class DepositFrame extends JFrame {
    public DepositFrame() {
        setTitle("Κατάθεση — Bank of TUC");
        setSize(520,360);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel p = new JPanel(new BorderLayout());
        p.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        add(p);

        JLabel head = new JLabel("Κατάθεση Χρημάτων", SwingConstants.CENTER);
        head.setFont(new Font("SansSerif", Font.BOLD,18));
        p.add(head, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridLayout(3,2,12,12));
        form.setBorder(BorderFactory.createEmptyBorder(10,40,20,40));
        form.add(new JLabel("Επιλογή λογαριασμού:"));
        JComboBox<String> cb = new JComboBox<>(new String[]{"GR12... (Ταμιευτήριο)","GR98... (Τρεχ.)"});
        form.add(cb);
        form.add(new JLabel("Ποσό (€):"));
        JTextField amount = new JTextField();
        form.add(amount);
        form.add(new JLabel("Σχόλιο:"));
        JTextField note = new JTextField();
        form.add(note);
        p.add(form, BorderLayout.CENTER);

        JButton submit = new JButton("Κατάθεση");
        submit.setBackground(new Color(0,102,204)); submit.setForeground(Color.WHITE);
        submit.addActionListener(e -> {
            // TODO: call backend deposit
            JOptionPane.showMessageDialog(this, "Κατάθεση " + amount.getText() + " € εκτελέστηκε.");
            dispose();
        });
        JPanel foot = new JPanel();
        foot.add(submit);
        p.add(foot, BorderLayout.SOUTH);
    }

    public static void main(String[] args){ SwingUtilities.invokeLater(() -> new DepositFrame().setVisible(true)); }
}
*/