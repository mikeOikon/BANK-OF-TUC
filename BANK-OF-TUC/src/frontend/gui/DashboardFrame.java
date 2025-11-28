package frontend.gui;

import javax.swing.*;
import java.awt.*;

public class DashboardFrame extends JFrame {

    public DashboardFrame() {
        setTitle("Bank of TUC - Πίνακας Ελέγχου");
        setSize(1000, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        Color mainBlue = new Color(0, 51, 102);
        Color lightBlue = new Color(220, 230, 250);

        // ===== ΚΥΡΙΟ PANEL =====
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        setContentPane(mainPanel);

        // ===== ΠΛΕΥΡΙΝΗ ΜΠΑΡΑ =====
        JPanel sidePanel = new JPanel();
        sidePanel.setPreferredSize(new Dimension(250, getHeight()));
        sidePanel.setBackground(mainBlue);
        sidePanel.setLayout(new BoxLayout(sidePanel, BoxLayout.Y_AXIS));
        sidePanel.setBorder(BorderFactory.createEmptyBorder(30, 20, 30, 20));

        // --- ΛΟΓΟΤΥΠΟ ---
        JLabel logoLabel = new JLabel("tuc bank", SwingConstants.CENTER);
        logoLabel.setFont(new Font("Arial", Font.BOLD, 26));
        logoLabel.setForeground(Color.WHITE);
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel logoIcon = new JLabel();
        logoIcon.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Προσπάθεια φόρτωσης εικόνας
        java.net.URL logoURL = getClass().getResource("/frontend/gui/552644274_1308216287470008_1862680383229436246_n.jpg");
        if (logoURL != null) {
            logoIcon.setIcon(new ImageIcon(logoURL));
        } else {
            logoIcon.setText("[LOGO]");
            logoIcon.setForeground(Color.WHITE);
            logoIcon.setFont(new Font("Arial", Font.BOLD, 20));
        }

        sidePanel.add(logoIcon);
        sidePanel.add(Box.createVerticalStrut(10));
        sidePanel.add(logoLabel);
        sidePanel.add(Box.createVerticalStrut(40));

        // --- Λίστα Λογαριασμών ---
        JLabel accountLabel = new JLabel("By Accounts");
        accountLabel.setFont(new Font("Arial", Font.BOLD, 14));
        accountLabel.setForeground(Color.WHITE);
        accountLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        sidePanel.add(accountLabel);
        sidePanel.add(Box.createVerticalStrut(10));

        String[] accounts = {"Account 1", "Account 2", "Account 3"};
        for (String acc : accounts) {
            JButton accButton = new JButton(acc);
            accButton.setFont(new Font("Arial", Font.PLAIN, 13));
            accButton.setForeground(mainBlue);
            accButton.setBackground(Color.WHITE);
            accButton.setFocusPainted(false);
            accButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            accButton.setMaximumSize(new Dimension(200, 35));
            accButton.setAlignmentX(Component.LEFT_ALIGNMENT);
            sidePanel.add(accButton);
            sidePanel.add(Box.createVerticalStrut(10));
        }

        sidePanel.add(Box.createVerticalStrut(40));

        // --- Profile και Settings ---
        JButton profileButton = new JButton("Profile");
        JButton settingsButton = new JButton("Settings");
        for (JButton btn : new JButton[]{profileButton, settingsButton}) {
            btn.setFont(new Font("Arial", Font.BOLD, 13));
            btn.setForeground(mainBlue);
            btn.setBackground(Color.WHITE);
            btn.setFocusPainted(false);
            btn.setMaximumSize(new Dimension(200, 35));
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btn.setAlignmentX(Component.LEFT_ALIGNMENT);
            sidePanel.add(btn);
            sidePanel.add(Box.createVerticalStrut(10));
        }

        mainPanel.add(sidePanel, BorderLayout.WEST);

        // ===== ΚΥΡΙΟ ΠΕΡΙΕΧΟΜΕΝΟ =====
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        // --- Επάνω τίτλοι: Balance + Statistics ---
        JPanel topPanel = new JPanel(new GridLayout(1, 2, 50, 0));
        topPanel.setBackground(Color.WHITE);

        // BALANCE PANEL
        JPanel balancePanel = new JPanel();
        balancePanel.setBackground(Color.WHITE);
        balancePanel.setLayout(new BoxLayout(balancePanel, BoxLayout.Y_AXIS));

        JLabel balanceTitle = new JLabel("BALANCE");
        balanceTitle.setFont(new Font("Arial", Font.BOLD, 18));
        balanceTitle.setForeground(mainBlue);

        JLabel balanceAmount = new JLabel("21,00 €");
        balanceAmount.setFont(new Font("Arial", Font.BOLD, 36));
        balanceAmount.setForeground(mainBlue);

        JLabel recentLabel = new JLabel("RECENT TRANSACTIONS");
        recentLabel.setFont(new Font("Arial", Font.BOLD, 13));
        recentLabel.setForeground(mainBlue);
        recentLabel.setBorder(BorderFactory.createEmptyBorder(15, 0, 10, 0));

        JLabel t1 = new JLabel("• Payment to Supermarket - €10");
        JLabel t2 = new JLabel("• Deposit from Payroll - €31");
        JLabel t3 = new JLabel("• Utility Bill Payment - €5");

        for (JLabel lbl : new JLabel[]{t1, t2, t3}) {
            lbl.setFont(new Font("Arial", Font.PLAIN, 12));
            lbl.setForeground(Color.DARK_GRAY);
        }

        JLabel viewMore = new JLabel("<html><u>VIEW FULL TRANSACTION HISTORY</u></html>");
        viewMore.setFont(new Font("Arial", Font.PLAIN, 12));
        viewMore.setForeground(mainBlue);
        viewMore.setCursor(new Cursor(Cursor.HAND_CURSOR));
        viewMore.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        balancePanel.add(balanceTitle);
        balancePanel.add(balanceAmount);
        balancePanel.add(recentLabel);
        balancePanel.add(t1);
        balancePanel.add(t2);
        balancePanel.add(t3);
        balancePanel.add(viewMore);

        // STATISTICS PANEL
        JPanel statsPanel = new JPanel(new BorderLayout());
        statsPanel.setBackground(Color.WHITE);
        JLabel statsTitle = new JLabel("STATISTICS");
        statsTitle.setFont(new Font("Arial", Font.BOLD, 18));
        statsTitle.setForeground(mainBlue);
        statsTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));

        JLabel statsImage = new JLabel();
        statsImage.setHorizontalAlignment(SwingConstants.CENTER);
        java.net.URL statsURL = getClass().getResource("/frontend/gui/stats_placeholder.png");
        if (statsURL != null) {
            statsImage.setIcon(new ImageIcon(statsURL));
        } else {
            statsImage.setText("[CHART]");
            statsImage.setFont(new Font("Arial", Font.BOLD, 20));
            statsImage.setForeground(mainBlue);
        }

        statsPanel.add(statsTitle, BorderLayout.NORTH);
        statsPanel.add(statsImage, BorderLayout.CENTER);

        topPanel.add(balancePanel);
        topPanel.add(statsPanel);

        contentPanel.add(topPanel, BorderLayout.CENTER);
        mainPanel.add(contentPanel, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            DashboardFrame frame = new DashboardFrame();
            frame.setVisible(true);
        });
    }
}
