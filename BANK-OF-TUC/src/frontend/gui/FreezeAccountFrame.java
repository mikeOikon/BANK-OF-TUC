package frontend.gui;

import javax.swing.*;
import java.awt.*;

public class FreezeAccountFrame extends JFrame {
    public FreezeAccountFrame() {
        setTitle("Πάγωμα λογαριασμού");
        setSize(520,260);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel p = new JPanel(new BorderLayout());
        p.setBorder(BorderFactory.createEmptyBorder(16,16,16,16));
        add(p);
        JLabel info = new JLabel("<html><center>Μπορείτε να παγώσετε προσωρινά τον λογαριασμό\nκαι τις συναλλαγές.</center></html>", SwingConstants.CENTER);
        p.add(info, BorderLayout.CENTER);
        JButton freeze = new JButton("Πάγωμα λογαριασμού");
        freeze.setBackground(new Color(204,51,51)); freeze.setForeground(Color.WHITE);
        freeze.addActionListener(e -> {
            // TODO: call backend to freeze account
            JOptionPane.showMessageDialog(this,"Ο λογαριασμός παγώθηκε προσωρινά.");
            dispose();
        });
        JPanel f = new JPanel(); f.add(freeze); p.add(f, BorderLayout.SOUTH);
    }

    public static void main(String[] args){ SwingUtilities.invokeLater(() -> new FreezeAccountFrame().setVisible(true)); }
}
