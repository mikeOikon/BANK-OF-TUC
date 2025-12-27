package frontend.gui;

import javax.swing.*;
import java.awt.*;

public class ViewUserFrame extends JFrame {
    public ViewUserFrame() {
        setTitle("Προβολή Στοιχείων Χρήστη");
        setSize(640,420);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel p = new JPanel(new GridLayout(4,2,12,12));
        p.setBorder(BorderFactory.createEmptyBorder(18,18,18,18));
        p.add(new JLabel("Όνομα:")); p.add(new JLabel(safe(UserSession.getInstance().getCurrentUser().getUsername())));
        p.add(new JLabel("Email:")); p.add(new JLabel(safe(UserSession.getInstance().getCurrentUser().getEmail())));
        p.add(new JLabel("Τηλέφωνο:")); p.add(new JLabel("6901234567"));
        p.add(new JLabel("Διεύθυνση:")); p.add(new JLabel("Χανιά, Κρήτη"));
        add(p);
    }
    private String safe(String s){ return s==null? "-" : s; }
    public static void main(String[] args){ SwingUtilities.invokeLater(() -> new ViewUserFrame().setVisible(true)); }
}
