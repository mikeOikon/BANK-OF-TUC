package frontend.gui;

import backend.users.User;
import frontend.gui.UserSession;

import javax.swing.*;
import java.awt.*;

public class DashboardFrame extends JFrame {

    private User user;

    public DashboardFrame() {
        this.user = UserSession.getInstance().getCurrentUser();

        setTitle("Bank of TUC — Πίνακας Ελέγχου");
        setSize(1100, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        Color mainBlue = new Color(0, 51, 102);
        Color card = new Color(245, 247, 250);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(Color.WHITE);
        setContentPane(root);

        //---------------------------------------------------------
        // Sidebar
        //---------------------------------------------------------
        JPanel sidebar = new JPanel();
        sidebar.setBackground(mainBlue);
        sidebar.setPreferredSize(new Dimension(260, 700));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBorder(BorderFactory.createEmptyBorder(24, 16, 16, 16));

        JLabel logo = new JLabel("<html><span style='color:#fff; font-weight:bold; font-size:18px;'>BANK OF TUC</span></html>");
        logo.setAlignmentX(Component.LEFT_ALIGNMENT);
        sidebar.add(logo);
        sidebar.add(Box.createVerticalStrut(20));

        JLabel userLabel = new JLabel("User: " + safe(user.getUsername()));
        userLabel.setForeground(Color.WHITE);
        userLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        sidebar.add(userLabel);
        sidebar.add(Box.createVerticalStrut(20));

        // Add sidebar buttons dynamically based on permissions
        if (user.canViewAccounts()) sidebar.add(navButton("Λογαριασμοί"));
        if (user.canTransferMoney()) sidebar.add(navButton("Μεταφορά"));
        if (user.canViewTransactionsHistory()) sidebar.add(navButton("Ιστορικό Συναλλαγών"));
        if (user.canRemoveUsers()) sidebar.add(navButton("Διαχείριση Χρηστών"));
        if (user.canPromoteUser() || user.canDemoteUser()) sidebar.add(navButton("Αναβάθμιση / Υποβάθμιση Χρηστών"));
        if (user.canAssistUsers()) sidebar.add(navButton("Βοήθεια Χρηστών"));
        if (user.canViewAllAccounts()) sidebar.add(navButton("Προβολή Όλων των Λογαριασμών"));

        root.add(sidebar, BorderLayout.WEST);

        //---------------------------------------------------------
        // Content area
        //---------------------------------------------------------
        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(Color.WHITE);
        content.setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));

        // Top summary card
        JPanel top = new JPanel(new GridLayout(1, 2, 18, 0));
        top.setOpaque(false);

        JPanel balCard = new JPanel(new BorderLayout());
        balCard.setBackground(card);
        balCard.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        JLabel balTitle = new JLabel("Υπόλοιπο");
        balTitle.setFont(new Font("SansSerif", Font.BOLD, 14));
        JLabel balVal = new JLabel("1.520,00 €"); // TODO: fetch real balance
        balVal.setFont(new Font("SansSerif", Font.BOLD, 28));
        balCard.add(balTitle, BorderLayout.NORTH);
        balCard.add(balVal, BorderLayout.CENTER);

        JPanel recentCard = new JPanel(new BorderLayout());
        recentCard.setBackground(card);
        recentCard.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        recentCard.add(new JLabel("Πρόσφατες Συναλλαγές"), BorderLayout.NORTH);
        DefaultListModel<String> lm = new DefaultListModel<>();
        lm.addElement("Κατάθεση +500 €");
        lm.addElement("Μεταφορά -150 €");
        JList<String> l = new JList<>(lm);
        recentCard.add(new JScrollPane(l), BorderLayout.CENTER);

        top.add(balCard);
        top.add(recentCard);
        content.add(top, BorderLayout.NORTH);

        // Center area: quick actions based on permissions
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 16));
        actions.setOpaque(false);

        if (user.canViewAccounts()) actions.add(actionButton("Άνοιγμα λογαριασμού"));
        if (user.canTransferMoney()) actions.add(actionButton("Νέα Μεταφορά"));
        if (user.canAssistUsers()) actions.add(actionButton("Βοήθεια Χρηστών"));
        if (user.canPromoteUser() || user.canDemoteUser()) actions.add(actionButton("Αναβάθμιση/Υποβάθμιση Χρηστών"));

        content.add(actions, BorderLayout.CENTER);
        root.add(content, BorderLayout.CENTER);
    }

    //---------------------------------------------------------
    // BUTTON STYLES
    //---------------------------------------------------------
    private JButton navButton(String t) {
        JButton b = new JButton(t);
        b.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        b.setAlignmentX(Component.LEFT_ALIGNMENT);
        b.setBackground(new Color(255, 255, 255));
        b.setForeground(new Color(0, 51, 102));
        b.setFocusPainted(false);
        return b;
    }

    private JButton actionButton(String t) {
        JButton b = new JButton(t);
        b.setBackground(new Color(0, 102, 204));
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setFont(new Font("SansSerif", Font.BOLD, 12));
        return b;
    }

    private String safe(String s) {
        return s == null ? "(guest)" : s;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new DashboardFrame().setVisible(true));
    }
}
