package frontend.gui;

import javax.swing.*;
import java.awt.*;

public class TransferFrame extends JFrame {

    public TransferFrame() {
        setTitle("Μεταφορά Χρημάτων");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        Color mainBlue = new Color(0, 51, 102);
        Color white = Color.WHITE;

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(mainBlue);
        add(mainPanel);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(mainBlue);
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        JButton backButton = new JButton("⟵ Πίσω");
        styleBackButton(backButton);
        topPanel.add(backButton, BorderLayout.WEST);

        JLabel titleLabel = new JLabel("Μεταφορά Χρημάτων", SwingConstants.CENTER);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        topPanel.add(titleLabel, BorderLayout.CENTER);

        mainPanel.add(topPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        centerPanel.setBackground(white);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        centerPanel.add(new JLabel("IBAN Παραλήπτη:"));
        JTextField ibanField = new JTextField();
        centerPanel.add(ibanField);

        centerPanel.add(new JLabel("Ποσό (€):"));
        JTextField amountField = new JTextField();
        centerPanel.add(amountField);

        centerPanel.add(new JLabel("Σκοπός:"));
        JTextField purposeField = new JTextField();
        centerPanel.add(purposeField);

        JButton transferButton = new JButton("Μεταφορά");
        transferButton.setBackground(new Color(0, 102, 204));
        transferButton.setForeground(Color.WHITE);
        transferButton.setFont(new Font("SansSerif", Font.BOLD, 16));
        transferButton.setFocusPainted(false);
        transferButton.addActionListener(e -> JOptionPane.showMessageDialog(this,
                "Μεταφορά " + amountField.getText() + " € προς " + ibanField.getText() + " ολοκληρώθηκε!"));

        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(white);
        bottomPanel.add(transferButton);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

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

    // main για δοκιμή
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TransferFrame().setVisible(true));
    }
}
