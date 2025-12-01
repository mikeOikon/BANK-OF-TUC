package frontend.gui;

import javax.swing.*;
import java.awt.*;

public class RecentTransactionsFrame extends JFrame {

    public RecentTransactionsFrame() {
        setTitle("Πρόσφατες Συναλλαγές");
        setSize(1000, 650);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        String[] columns = {"Ημερομηνία", "Περιγραφή", "Ποσό (€)", "Υπόλοιπο (€)"};
        Object[][] data = {
                {"12/11/2025", "Κατάθεση", "500.00", "2,020.00"},
                {"11/11/2025", "Μεταφορά", "-150.00", "1,520.00"},
                {"10/11/2025", "Πληρωμή Λογαριασμού", "-70.00", "1,670.00"}
        };

        JTable table = new JTable(data, columns);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new RecentTransactionsFrame().setVisible(true));
    }
}
