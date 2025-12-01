package frontend.gui;

import javax.swing.*;
import java.awt.*;

public class AccountFrame extends JFrame {

    public AccountFrame() {
        setTitle("Bank of TUC - Λογαριασμοί");
        setSize(1000, 650);
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

        JLabel titleLabel = new JLabel("Ο Λογαριασμός μου", SwingConstants.CENTER);
        titleLabel.setForeground(white);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 28));
        topPanel.add(titleLabel, BorderLayout.CENTER);

        mainPanel.add(topPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel();
        centerPanel.setBackground(white);
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(60, 100, 60, 100));
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        JPanel infoPanel = new JPanel(new GridLayout(4, 2, 15, 15));
        infoPanel.setBackground(white);
        infoPanel.setMaximumSize(new Dimension(600, 200));

        infoPanel.add(createInfoLabel("IBAN:"));
        infoPanel.add(createValueLabel("GR12 3456 7890 1234 5678 9012 345"));
        infoPanel.add(createInfoLabel("Τύπος Λογαριασμού:"));
        infoPanel.add(createValueLabel("Προσωπικός"));
        infoPanel.add(createInfoLabel("Υπόλοιπο:"));
        infoPanel.add(createValueLabel("1,520.00 €"));
        infoPanel.add(createInfoLabel("Κατάσταση:"));
        infoPanel.add(createValueLabel("Ενεργός"));

        centerPanel.add(infoPanel);
        centerPanel.add(Box.createVerticalStrut(60));

        JPanel actionsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 20));
        actionsPanel.setBackground(white);

        JButton depositButton = createStyledButton("Κατάθεση");
        JButton transferButton = createStyledButton("Μεταφορά");
        JButton historyButton = createStyledButton("Ιστορικό Κινήσεων");

        actionsPanel.add(depositButton);
        actionsPanel.add(transferButton);
        actionsPanel.add(historyButton);

        centerPanel.add(actionsPanel);

        depositButton.addActionListener(e -> new DepositFrame().setVisible(true));
        transferButton.addActionListener(e -> new TransferFrame().setVisible(true));
        historyButton.addActionListener(e -> new RecentTransactionsFrame().setVisible(true));

        backButton.addActionListener(e -> dispose());
    }

    private JLabel createInfoLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("SansSerif", Font.BOLD, 16));
        label.setForeground(new Color(0, 51, 102));
        return label;
    }

    private JLabel createValueLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("SansSerif", Font.PLAIN, 16));
        label.setForeground(Color.DARK_GRAY);
        return label;
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setFont(new Font("SansSerif", Font.BOLD, 15));
        button.setBackground(new Color(0, 102, 204));
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return button;
    }

    private void styleBackButton(JButton backButton) {
        backButton.setBackground(new Color(255, 255, 255, 40));
        backButton.setForeground(Color.WHITE);
        backButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        backButton.setFocusPainted(false);
        backButton.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        backButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AccountFrame().setVisible(true));
    }
}
