package frontend.gui;

import javax.swing.*;
import java.awt.*;

public class DepositFrame extends JFrame {

    public DepositFrame() {
        setTitle("Κατάθεση Χρημάτων");
        setSize(500, 350);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        Color mainBlue = new Color(0, 51, 102);
        Color white = Color.WHITE;

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(mainBlue);
        add(mainPanel);

        // Top panel
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(mainBlue);
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        JButton backButton = new JButton("⟵ Πίσω");
        styleBackButton(backButton);
        topPanel.add(backButton, BorderLayout.WEST);

        JLabel titleLabel = new JLabel("Κατάθεση Χρημάτων", SwingConstants.CENTER);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        topPanel.add(titleLabel, BorderLayout.CENTER);

        mainPanel.add(topPanel, BorderLayout.NORTH);

        // Center panel για ποσό
        JPanel centerPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        centerPanel.setBackground(white);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        centerPanel.add(new JLabel("Ποσό (€):"));
        JTextField amountField = new JTextField();
        centerPanel.add(amountField);

        JButton depositButton = new JButton("Κατάθεση");
        depositButton.setBackground(new Color(0, 102, 204));
        depositButton.setForeground(Color.WHITE);
        depositButton.setFont(new Font("SansSerif", Font.BOLD, 16));
        depositButton.setFocusPainted(false);
        depositButton.addActionListener(e -> JOptionPane.showMessageDialog(this, "Κατάθεση " + amountField.getText() + " € ολοκληρώθηκε!"));

        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(white);
        bottomPanel.add(depositButton);
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
        SwingUtilities.invokeLater(() -> new DepositFrame().setVisible(true));
    }
}
