package backend.transactions;

import java.time.LocalDateTime;

import types.TransactionType;

public abstract class Transaction {

    protected TransactionType type;
    protected double amount;

    protected transient LocalDateTime timestamp; // δεν αποθηκεύεται στο JSON
    protected String timestampString; // αυτό αποθηκεύεται
    protected String fromAccountIban;
    protected String toAccountIban;

    public Transaction(TransactionType type, double amount,
                       String fromAccountIban, String toAccountIban) {

        this.type = type;
        this.amount = amount;
        this.timestamp = LocalDateTime.now();
        this.timestampString = this.timestamp.toString(); // ISO string

        this.fromAccountIban = fromAccountIban;
        this.toAccountIban = toAccountIban;
    }

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

    @Override
    public String toString() {
        return "[" + getTimestamp() + "] "
                + type + " "
                + amount +
                " from " + fromAccountIban +
                " to " + toAccountIban;
    }
}
