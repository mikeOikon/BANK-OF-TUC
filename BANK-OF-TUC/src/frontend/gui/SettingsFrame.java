package frontend.gui;

import javax.swing.*;
import java.awt.*;

public class SettingsFrame extends JFrame {

    public SettingsFrame() {
        setTitle("Bank of TUC - Ρυθμίσεις");
        setSize(1000, 650);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Χρωματική παλέτα
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

        // Κουμπί Πίσω
        JButton backButton = new JButton("⟵ Πίσω");
        styleBackButton(backButton);
        topPanel.add(backButton, BorderLayout.WEST);

        // Τίτλος
        JLabel titleLabel = new JLabel("Ρυθμίσεις", SwingConstants.CENTER);
        titleLabel.setForeground(white);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 28));
        topPanel.add(titleLabel, BorderLayout.CENTER);

        mainPanel.add(topPanel, BorderLayout.NORTH);

        // Κεντρικό panel με κουμπιά
        JPanel centerPanel = new JPanel();
        centerPanel.setBackground(white);
        centerPanel.setLayout(new GridLayout(4, 1, 30, 20));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(60, 300, 60, 300));
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        // Κουμπιά επιλογών με νέα σειρά
        JButton viewUserButton = createStyledButton("Προβολή στοιχείων χρήστη");
        JButton editUserButton = createStyledButton("Αλλαγή στοιχείων χρήστη");
        JButton changePassButton = createStyledButton("Αλλαγή κωδικού πρόσβασης");
        JButton freezeButton = createStyledButton("Πάγωμα λογαριασμού / Συναλλαγών");

        centerPanel.add(viewUserButton);
        centerPanel.add(editUserButton);
        centerPanel.add(changePassButton);
        centerPanel.add(freezeButton);

        // Λειτουργίες κουμπιών
        viewUserButton.addActionListener(e ->
                JOptionPane.showMessageDialog(this, "Προβολή στοιχείων χρήστη..."));
        editUserButton.addActionListener(e ->
                JOptionPane.showMessageDialog(this, "Αλλαγή στοιχείων χρήστη..."));
        changePassButton.addActionListener(e ->
                JOptionPane.showMessageDialog(this, "Αλλαγή κωδικού πρόσβασης..."));
        freezeButton.addActionListener(e ->
                JOptionPane.showMessageDialog(this, "Ο λογαριασμός και οι συναλλαγές έχουν παγώσει προσωρινά."));

        // Επιστροφή (Πίσω)
        backButton.addActionListener(e -> dispose());
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setFont(new Font("SansSerif", Font.BOLD, 16));
        button.setBackground(new Color(0, 102, 204));
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0, 51, 102), 2),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(0, 76, 153));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(0, 102, 204));
            }
        });
        return button;
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
        SwingUtilities.invokeLater(() -> new SettingsFrame().setVisible(true));
    }
}
