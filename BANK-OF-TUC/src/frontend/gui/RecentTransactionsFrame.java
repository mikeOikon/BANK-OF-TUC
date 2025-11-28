package frontend.gui;

import javax.swing.*;
import java.awt.*;

public class RecentTransactionsFrame extends JFrame {

    public RecentTransactionsFrame() {
        setTitle("Bank of TUC - Πρόσφατες Συναλλαγές");
        setSize(1000, 650);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Χρώματα
        Color mainBlue = new Color(0, 51, 102);
        Color white = Color.WHITE;

        // Κύριο panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(mainBlue);
        add(mainPanel);

        // Επικεφαλίδα
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(mainBlue);
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        JButton backButton = new JButton("⟵ Πίσω");
        styleBackButton(backButton);
        topPanel.add(backButton, BorderLayout.WEST);

        JLabel titleLabel = new JLabel("Πρόσφατες Συναλλαγές", SwingConstants.CENTER);
        titleLabel.setForeground(white);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 28));
        topPanel.add(titleLabel, BorderLayout.CENTER);

        mainPanel.add(topPanel, BorderLayout.NORTH);

        // Κεντρικό panel για λίστα συναλλαγών
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(white);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        // Παράδειγμα πίνακα συναλλαγών
        String[] columns = {"Ημερομηνία", "Περιγραφή", "Ποσό (€)", "Υπόλοιπο (€)"};
        Object[][] data = {
                {"12/11/2025", "Κατάθεση", "500.00", "2,020.00"},
                {"11/11/2025", "Μεταφορά", "-150.00", "1,520.00"},
                {"10/11/2025", "Πληρωμή Λογαριασμού", "-70.00", "1,670.00"}
        };

        JTable transactionsTable = new JTable(data, columns);
        transactionsTable.setFillsViewportHeight(true);
        transactionsTable.setRowHeight(25);
        transactionsTable.setFont(new Font("SansSerif", Font.PLAIN, 14));
        transactionsTable.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));
        transactionsTable.setSelectionBackground(new Color(0, 102, 204));
        transactionsTable.setSelectionForeground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(transactionsTable);
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        // Επιστροφή (Πίσω)
        backButton.addActionListener(e -> dispose());
    }

    private void styleBackButton(JButton backButton) {
        backButton.setBackground(new Color(255, 255, 255, 40));
        backButton.setForeground(Color.WHITE);
        backButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        backButton.setFocusPainted(false);
        backButton.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        backButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        backButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                backButton.setBackground(new Color(255, 255, 255, 70));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                backButton.setBackground(new Color(255, 255, 255, 40));
            }
        });
    }

    // Test run
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new RecentTransactionsFrame().setVisible(true));
    }
}
