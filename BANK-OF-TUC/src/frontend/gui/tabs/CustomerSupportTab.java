package frontend.gui.tabs;

import backend.BankSystem;
import backend.support.SupportTicket;
import backend.users.User;

import javax.swing.*;
import java.awt.*;

public class CustomerSupportTab extends JPanel {

    private final User employee;

    private final DefaultListModel<SupportTicket> ticketListModel = new DefaultListModel<>();
    private final JList<SupportTicket> ticketList = new JList<>(ticketListModel);
    private final JTextArea messagesArea = new JTextArea();
    private final JTextField replyField = new JTextField();

    public CustomerSupportTab(User employee) {
        this.employee = employee;

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        add(createTitle(), BorderLayout.NORTH);
        add(createMainPanel(), BorderLayout.CENTER);
        add(createReplyPanel(), BorderLayout.SOUTH);

        loadTickets();
        setupListeners();
    }

    private JLabel createTitle() {
        JLabel title = new JLabel("Customer Support");
        title.setFont(new Font("SansSerif", Font.BOLD, 18));
        return title;
    }

    private JSplitPane createMainPanel() {
        // Ticket list
        ticketList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane ticketScroll = new JScrollPane(ticketList);
        ticketScroll.setPreferredSize(new Dimension(250, 0));

        // Messages area
        messagesArea.setEditable(false);
        messagesArea.setLineWrap(true);
        messagesArea.setWrapStyleWord(true);
        JScrollPane messagesScroll = new JScrollPane(messagesArea);

        return new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, ticketScroll, messagesScroll);
    }

    private JPanel createReplyPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 0));
        JButton replyButton = new JButton("Απάντηση");

        panel.add(replyField, BorderLayout.CENTER);
        panel.add(replyButton, BorderLayout.EAST);

        replyButton.addActionListener(e -> sendReply());

        return panel;
    }

    private void loadTickets() {
        ticketListModel.clear();
        for (SupportTicket ticket : BankSystem.getInstance().getAllTickets()) {
            ticketListModel.addElement(ticket);
        }
    }

    private void setupListeners() {
        ticketList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                showSelectedTicket();
            }
        });
    }

    private void showSelectedTicket() {
        SupportTicket ticket = ticketList.getSelectedValue();
        if (ticket == null) {
            messagesArea.setText("");
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Ticket ID: ").append(ticket.getTicketId()).append("\n");
        sb.append("Customer: ").append(ticket.getCustomerId()).append("\n");
        sb.append("Status: ").append(ticket.getStatus()).append("\n\n");

        for (String msg : ticket.getMessages()) {
            sb.append(msg).append("\n\n");
        }

        messagesArea.setText(sb.toString());
    }

    private void sendReply() {
        SupportTicket ticket = ticketList.getSelectedValue();
        String reply = replyField.getText().trim();

        if (ticket == null || reply.isEmpty()) return;

        ticket.getMessages().add("EMPLOYEE: " + reply);
        replyField.setText("");
        showSelectedTicket();
    }
}
