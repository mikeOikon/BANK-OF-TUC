package frontend.gui;

import javax.swing.*;
import java.awt.*;

public class ViewUserFrame extends JFrame {

    public ViewUserFrame() {
        setTitle("Προβολή Στοιχείων Χρήστη");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        panel.add(new JLabel("Όνομα:"));
        panel.add(new JLabel(UserSession.getInstance().getUsername()));
        panel.add(new JLabel("Email:"));
        panel.add(new JLabel(UserSession.getInstance().getEmail()));
        panel.add(new JLabel("Τηλέφωνο:"));
        panel.add(new JLabel("6901234567"));
        panel.add(new JLabel("Διεύθυνση:"));
        panel.add(new JLabel("Χανιά, Κρήτη"));

        add(panel);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ViewUserFrame().setVisible(true));
    }
}
