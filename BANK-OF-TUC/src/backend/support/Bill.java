package backend.support;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Bill {
    private String paymentCode;    // Ο μοναδικός κωδικός RF
    private String businessIBAN;   // Ο λογαριασμός που θα λάβει τα χρήματα
    private String businessName;   // Το όνομα της επιχείρησης (π.χ. "ΔΕΗ")
    private double amount;         // Το ποσό της οφειλής
    private String description;    // Αιτιολογία (π.χ. "Λογαριασμός Ρεύματος")
    private String issueDate;      // Ημερομηνία έκδοσης
    private boolean paid;          // Κατάσταση πληρωμής

    public Bill(String paymentCode, String businessIBAN, String businessName, double amount, String description) {
        this.paymentCode = paymentCode;
        this.businessIBAN = businessIBAN;
        this.businessName = businessName;
        this.amount = amount;
        this.description = description;
        this.paid = false;
        
        // Αυτόματη καταγραφή ημερομηνίας έκδοσης
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        this.issueDate = dtf.format(LocalDateTime.now());
    }

    // --- Getters & Setters ---

    public String getPaymentCode() {
        return paymentCode;
    }

    public String getBusinessIBAN() {
        return businessIBAN;
    }

    public String getBusinessName() {
        return businessName;
    }

    public double getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }

    public boolean isPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    public String getIssueDate() {
        return issueDate;
    }

    /**
     * Επιστρέφει μια καλαίσθητη περιγραφή του λογαριασμού για το GUI.
     */
    @Override
    public String toString() {
        return String.format("Bill [%s] - %s: %.2f€ (%s)", 
            paymentCode, businessName, amount, (paid ? "PAID" : "UNPAID"));
    }
}