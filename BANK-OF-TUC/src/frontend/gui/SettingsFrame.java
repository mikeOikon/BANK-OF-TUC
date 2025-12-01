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

        Color mainBlue = new Color(0, 51, 102);
        Color white = Color.WHITE;

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(white);
        setContentPane(mainPanel);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(mainBlue);
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        JButton backButton = new JButton("⟵ Πίσω");
        topPanel.add(backButton, BorderLayout.WEST);

        JLabel titleLabel = new JLabel("Ρυθμίσεις", SwingConstants.CENTER);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 28));
        topPanel.add(titleLabel, BorderLayout.CENTER);

        mainPanel.add(topPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridLayout(4, 1, 30, 20));
        centerPanel.setBackground(white);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(60, 300, 60, 300));
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        JButton viewUserButton = createButton("Προβολή στοιχείων χρήστη");
        JButton editUserButton = createButton("Αλλαγή στοιχείων χρήστη");
        JButton changePassButton = createButton("Αλλαγή κωδικού πρόσβασης");
        JButton freezeButton = createButton("Πάγωμα λογαριασμού / Συναλλαγών");

        centerPanel.add(viewUserButton);
        centerPanel.add(editUserButton);
        centerPanel.add(changePassButton);
        centerPanel.add(freezeButton);

        viewUserButton.addActionListener(e -> new ViewUserFrame().setVisible(true));
        editUserButton.addActionListener(e -> new EditUserFrame().setVisible(true));
        changePassButton.addActionListener(e -> new ChangePasswordFrame().setVisible(true));
        freezeButton.addActionListener(e -> new FreezeAccountFrame().setVisible(true));

        backButton.addActionListener(e -> dispose());
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("SansSerif", Font.BOLD, 16));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(0, 102, 204));
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return button;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SettingsFrame().setVisible(true));
    }
}
