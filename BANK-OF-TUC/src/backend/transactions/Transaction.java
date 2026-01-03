package backend.transactions;

import java.time.LocalDateTime;

import backend.BankSystem;
import types.TransactionType;

public class Transaction {

    protected final TransactionType type;
    protected final double amount;

    protected transient LocalDateTime timestamp; // δεν αποθηκεύεται στο JSON
    protected String timestampString;             // αποθηκεύεται στο JSON

    protected final String fromAccountIban;
    protected final String toAccountIban;

    protected String description; 

    protected Transaction(
            TransactionType type,
            double amount,
            String fromAccountIban,
            String toAccountIban,
            String description
    ) {
        this.type = type;
        this.amount = amount;
        this.fromAccountIban = fromAccountIban;
        this.toAccountIban = toAccountIban;
        this.description = description;

        this.timestamp = BankSystem.getInstance().getSimulatedNow();
        this.timestampString = this.timestamp.toString();
    }

    // ---------------- GETTERS ----------------

    public TransactionType getType() {
        return type;
    }

    public double getAmount() {
        return amount;
    }

    public LocalDateTime getTimestamp() {
        if (timestamp == null && timestampString != null) {
            timestamp = LocalDateTime.parse(timestampString);
        }
        return timestamp;
    }

    public String getFromAccountIban() {
        return fromAccountIban;
    }

    public String getToAccountIban() {
        return toAccountIban;
    }

    public String getDescription() {
        return description;
    }

    // ---------------- DISPLAY ----------------

    @Override
    public String toString() {
        return "[" + getTimestamp() + "] "
                + formatByType();
    }

    protected String formatByType() {
        return switch (type) {

            case TRANSFER ->
                    "TRANSFER " + amount +
                    " from " + fromAccountIban +
                    " to " + toAccountIban;

            case DEPOSIT ->
                    "DEPOSIT " + amount +
                    " to " + toAccountIban;

            case WITHDRAW ->
                    "WITHDRAW " + amount +
                    " from " + fromAccountIban;

            case BILL_PAYMENT ->
                    "BILL_PAYMENT " + amount +
                    " from " + fromAccountIban +
                    " to " + toAccountIban;

            case AUTO_BILL_PAYMENT ->
                    "AUTO_BILL_PAYMENT " + amount +
                    " to " + toAccountIban;

            case INTEREST ->
                    "INTEREST " + amount +
                    " to " + toAccountIban;

            case FEE ->
                    "FEE " + amount +
                    " from " + fromAccountIban;
        };
    }
}
