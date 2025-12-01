package frontend.gui;

import javax.swing.*;
import java.awt.*;

public class FreezeAccountFrame extends JFrame {

    public FreezeAccountFrame() {
        setTitle("Πάγωμα Λογαριασμού");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JLabel label = new JLabel("Πατήστε το κουμπί για να παγώσετε τον λογαριασμό", SwingConstants.CENTER);
        JButton freezeButton = new JButton("Πάγωμα");
        freezeButton.addActionListener(e -> JOptionPane.showMessageDialog(this, "Λογαριασμός παγώθηκε!"));

        setLayout(new BorderLayout());
        add(label, BorderLayout.CENTER);
        add(freezeButton, BorderLayout.SOUTH);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FreezeAccountFrame().setVisible(true));
    }
}
