package frontend.gui;

import javax.swing.*;
import java.awt.*;

public class AccountFrame extends JFrame {
    public AccountFrame() {
        setTitle("Λογαριασμοί — Bank of TUC");
        setSize(920,620);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(Color.WHITE);
        setContentPane(root);

        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(new Color(0,51,102));
        top.setPreferredSize(new Dimension(0,80));
        JLabel title = new JLabel("Οι Λογαριασμοί μου", SwingConstants.CENTER);
        title.setForeground(Color.WHITE); title.setFont(new Font("SansSerif", Font.BOLD,20));
        top.add(title, BorderLayout.CENTER);
        root.add(top, BorderLayout.NORTH);

        JPanel center = new JPanel(new BorderLayout());
        center.setBorder(BorderFactory.createEmptyBorder(18,18,18,18));
        center.setBackground(Color.WHITE);

        // accounts list (left)
        DefaultListModel<String> lm = new DefaultListModel<>();
        lm.addElement("GR12 3456 7890 1234 - Ταμιευτήριο - 1.520,00 €");
        lm.addElement("GR98 1111 2222 3333 - Τρεχούμενο - 200,00 €");
        JList<String> accounts = new JList<>(lm);
        accounts.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        accounts.setFixedCellHeight(40);
        center.add(new JScrollPane(accounts), BorderLayout.WEST);

        // details (right)
        JPanel det = new JPanel();
        det.setLayout(new BoxLayout(det, BoxLayout.Y_AXIS));
        det.setBorder(BorderFactory.createEmptyBorder(10,20,10,10));
        det.setBackground(Color.WHITE);
        det.add(new JLabel("IBAN: GR12 3456 ..."));
        det.add(Box.createVerticalStrut(8));
        det.add(new JLabel("Τύπος: Ταμιευτήριο"));
        det.add(Box.createVerticalStrut(8));
        det.add(new JLabel("Υπόλοιπο: 1.520,00 €"));
        det.add(Box.createVerticalStrut(18));
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT,12,12));
        actions.setBackground(Color.WHITE);
        JButton dep = new JButton("Κατάθεση"); dep.addActionListener(e -> new DepositFrame().setVisible(true));
        JButton trans = new JButton("Μεταφορά"); trans.addActionListener(e -> new TransferFrame().setVisible(true));
        JButton hist = new JButton("Ιστορικό"); hist.addActionListener(e -> new RecentTransactionsFrame().setVisible(true));
        actions.add(dep); actions.add(trans); actions.add(hist);
        det.add(actions);

        center.add(det, BorderLayout.CENTER);

        root.add(center, BorderLayout.CENTER);
    }

    public static void main(String[] args) { SwingUtilities.invokeLater(() -> new AccountFrame().setVisible(true)); }
}
