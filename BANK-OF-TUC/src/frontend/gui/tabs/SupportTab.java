package frontend.gui.tabs;

import backend.BankSystem;
import backend.support.SupportTicket;
import backend.users.User;
import frontend.gui.UserSession;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class SupportTab extends JPanel {

    private final JTextArea historyArea;
    private final JTextArea inputArea;

    public SupportTab(User user) {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // ===== ΙΣΤΟΡΙΚΟ ΜΗΝΥΜΑΤΩΝ =====
        historyArea = new JTextArea();
        historyArea.setEditable(false);
        historyArea.setLineWrap(true);
        historyArea.setWrapStyleWord(true);

        JScrollPane historyScroll = new JScrollPane(historyArea);
        historyScroll.setBorder(BorderFactory.createTitledBorder("Τελευταία μηνύματα"));

        add(historyScroll, BorderLayout.CENTER);

        // ===== ΚΑΤΩ ΜΕΡΟΣ =====
        JPanel bottom = new JPanel(new BorderLayout(5, 5));

        inputArea = new JTextArea(3, 40);
        inputArea.setLineWrap(true);
        inputArea.setWrapStyleWord(true);

        JScrollPane inputScroll = new JScrollPane(inputArea);
        inputScroll.setBorder(BorderFactory.createTitledBorder("Νέο μήνυμα"));

        JButton sendBtn = new JButton("Αποστολή");

        bottom.add(inputScroll, BorderLayout.CENTER);
        bottom.add(sendBtn, BorderLayout.EAST);

        add(bottom, BorderLayout.SOUTH);

        // ===== ΦΟΡΤΩΣΗ ΥΠΑΡΧΟΝΤΟΣ TICKET =====
        loadMessages();

        // ===== ACTION =====
        sendBtn.addActionListener(e -> sendMessage());
    }

    private void loadMessages() {
        historyArea.setText("");

        BankSystem bank = BankSystem.getInstance();
        String userId = UserSession.getInstance().getCurrentUser().getUserID();

        SupportTicket ticket = bank.getTicketForCustomer(userId);

        if (ticket == null) {
            historyArea.setText("Δεν υπάρχει ενεργό αίτημα υποστήριξης.\n");
            return;
        }

        List<String> msgs = ticket.getMessages();
        int start = Math.max(0, msgs.size() - 3);

        for (int i = start; i < msgs.size(); i++) {
            historyArea.append(msgs.get(i) + "\n\n");
        }   
    }

    private void sendMessage() {
        String msg = inputArea.getText().trim();
        if (msg.isEmpty()) return;

        BankSystem bank = BankSystem.getInstance();
        String userId = UserSession.getInstance().getCurrentUser().getUserID();

        SupportTicket ticket = bank.getTicketForCustomer(userId);

        if (ticket == null) {
            ticket = new SupportTicket( bank.generateTicketID(), userId," Πελάτης: " + msg);
            bank.addTicket(ticket);
        } else {
            ticket.addMessage(" Πελάτης: " + msg);
        }

        bank.dao.save(bank);
        inputArea.setText("");
        loadMessages();
    }
}
