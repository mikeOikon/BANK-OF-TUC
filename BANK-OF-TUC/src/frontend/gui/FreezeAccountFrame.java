package frontend.gui;

import javax.swing.*;
import java.awt.*;

public class FreezeAccountFrame extends JFrame {

    public FreezeAccountFrame() {
        setTitle("Πάγωμα λογαριασμού / Συναλλαγών");
        setSize(500, 300);
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

        JLabel titleLabel = new JLabel("Πάγωμα λογαριασμού / Συναλλαγών", SwingConstants.CENTER);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        topPanel.add(titleLabel, BorderLayout.CENTER);

        mainPanel.add(topPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel();
        centerPanel.setBackground(white);
        centerPanel.setLayout(new BorderLayout());
        centerPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        JLabel infoLabel = new JLabel("<html>Πατήστε το κουμπί για να παγώσετε τον λογαριασμό και τις συναλλαγές προσωρινά.</html>", SwingConstants.CENTER);
        infoLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        centerPanel.add(infoLabel, BorderLayout.CENTER);

        JButton freezeButton = new JButton("Πάγωμα");
        freezeButton.setBackground(new Color(0, 102, 204));
        freezeButton.setForeground(Color.WHITE);
        freezeButton.setFont(new Font("SansSerif", Font.BOLD, 16));
        freezeButton.setFocusPainted(false);
        freezeButton.addActionListener(e -> JOptionPane.showMessageDialog(this, "Ο λογαριασμός και οι συναλλαγές έχουν παγώσει!"));

        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(white);
        bottomPanel.add(freezeButton);
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

    // ✅ main για εκτέλεση
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FreezeAccountFrame().setVisible(true));
    }
}
