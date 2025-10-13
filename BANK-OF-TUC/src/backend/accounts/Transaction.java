package backend.accounts;
import java.time.LocalDateTime;

public class Transaction {

	private String type; // π.χ. "deposit", "withdrawal", "transfer"
    private double amount;
    private  transient LocalDateTime timestamp;  // to json to skiparei ayto kai to apothikeuei ws string
    private String timeStampString;
    private String fromAccountIban;
    private String toAccountIban;

    public Transaction(String type, double amount, String fromAccountIban, String toAccountIban) {
        this.type = type;	
        this.amount = amount;
        this.timestamp = LocalDateTime.now(); // Καταγράφει την ώρα της συναλλαγής
        this.timeStampString = this.timestamp.toString();
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
