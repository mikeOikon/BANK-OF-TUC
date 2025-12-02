package frontend.gui;

import javax.swing.*;
import java.awt.*;

public class RecentTransactionsFrame extends JFrame {
    public RecentTransactionsFrame() {
        setTitle("Ιστορικό Συναλλαγών");
        setSize(980,640);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        String[] cols = {"Ημερομηνία","Περιγραφή","Ποσό (€)","Υπόλοιπο (€)"};
        Object[][] data = {
                {"12/11/2025","Κατάθεση","500.00","2.020,00"},
                {"11/11/2025","Μεταφορά","-150.00","1.520,00"},
                {"10/11/2025","Πληρωμή λογαριασμού","-70.00","1.670,00"}
        };

        JTable table = new JTable(data, cols);
        table.setRowHeight(26);
        JScrollPane sp = new JScrollPane(table);
        add(sp);
    }

    public static void main(String[] args){ SwingUtilities.invokeLater(() -> new RecentTransactionsFrame().setVisible(true)); }
}
