package backend.accounts;
import java.time.LocalDateTime;

public class Transaction {

	private String type; // π.χ. "deposit", "withdrawal", "transfer"
    private double amount;
    private LocalDateTime timestamp;
    private String fromAccountIban;
    private String toAccountIban;

    public Transaction(String type, double amount, String fromAccountIban, String toAccountIban) {
        this.type = type;	
        this.amount = amount;
        this.timestamp = LocalDateTime.now(); // Καταγράφει την ώρα της συναλλαγής
        this.fromAccountIban = fromAccountIban;
        this.toAccountIban = toAccountIban;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return "[" + timestamp + "] " + type + " " + amount +
               " from " + fromAccountIban + " to " + toAccountIban;
    }
}
