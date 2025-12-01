package frontend.gui;

import javax.swing.*;
import java.awt.*;

public class DashboardFrame extends JFrame {

    public DashboardFrame() {
        setTitle("Bank of TUC - Dashboard");
        setSize(1000, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        Color mainBlue = new Color(0, 51, 102);
        Color white = Color.WHITE;

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(white);
        setContentPane(mainPanel);

        JPanel sidePanel = new JPanel();
        sidePanel.setBackground(mainBlue);
        sidePanel.setPreferredSize(new Dimension(250, getHeight()));
        sidePanel.setLayout(new BoxLayout(sidePanel, BoxLayout.Y_AXIS));
        sidePanel.setBorder(BorderFactory.createEmptyBorder(30, 20, 30, 20));

        JLabel welcomeLabel = new JLabel("Καλώς ήρθες, " + UserSession.getInstance().getUsername(), SwingConstants.CENTER);
        welcomeLabel.setForeground(Color.WHITE);
        welcomeLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        sidePanel.add(welcomeLabel);
        sidePanel.add(Box.createVerticalStrut(40));

        JButton accountBtn = createButton("Λογαριασμοί");
        JButton depositBtn = createButton("Κατάθεση");
        JButton withdrawBtn = createButton("Ανάληψη");
        JButton transferBtn = createButton("Μεταφορά");
        JButton settingsBtn = createButton("Ρυθμίσεις");

        sidePanel.add(accountBtn);
        sidePanel.add(Box.createVerticalStrut(15));
        sidePanel.add(depositBtn);
        sidePanel.add(Box.createVerticalStrut(15));
        sidePanel.add(withdrawBtn);
        sidePanel.add(Box.createVerticalStrut(15));
        sidePanel.add(transferBtn);
        sidePanel.add(Box.createVerticalStrut(15));
        sidePanel.add(settingsBtn);

        mainPanel.add(sidePanel, BorderLayout.WEST);

        // Event listeners για navigation
        accountBtn.addActionListener(e -> new AccountFrame().setVisible(true));
        depositBtn.addActionListener(e -> new DepositFrame().setVisible(true));
        
        transferBtn.addActionListener(e -> new TransferFrame().setVisible(true));
        settingsBtn.addActionListener(e -> new SettingsFrame().setVisible(true));
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("SansSerif", Font.BOLD, 14));
        button.setForeground(new Color(0, 51, 102));
        button.setBackground(Color.WHITE);
        button.setFocusPainted(false);
        button.setMaximumSize(new Dimension(200, 40));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return button;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new DashboardFrame().setVisible(true));
    }
}
