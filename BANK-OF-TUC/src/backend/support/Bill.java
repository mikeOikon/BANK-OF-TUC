package backend.support;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Bill {

    protected String paymentCode;        // RF code (μοναδικός)
    private String businessIBAN;         // Λογαριασμός επιχείρησης
    private String businessName;         // Όνομα επιχείρησης
    protected String customerIBAN;       // Προεπιλεγμένος λογαριασμός πελάτη

    private double amount;               // Ποσό
    private String description;          // Αιτιολογία
    private String issueDate;            // Ημερομηνία έκδοσης

    protected boolean paid;              // Έχει πληρωθεί;
    
    // --- Auto Pay Fields ---
    private boolean autoPay;             // Να πληρώνεται αυτόματα;
    private String autoPayAccountIBAN;   // Από ποιον λογαριασμό θα γίνει η αυτόματη πληρωμή;

    // 👉 ΜΟΝΟ για μηνιαίους λογαριασμούς
    private String subscriptionId;       // null = ΟΧΙ μηνιαίος

    public Bill(
            String paymentCode,
            String businessIBAN,
            String businessName,
            double amount,
            String description
    ) {
        this.paymentCode = paymentCode;
        this.businessIBAN = businessIBAN;
        this.businessName = businessName;
        this.amount = amount;
        this.description = description;

        this.paid = false;
        this.autoPay = false;
        this.autoPayAccountIBAN = null; // Default null
        this.customerIBAN = null;
        this.subscriptionId = null;

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        this.issueDate = dtf.format(LocalDateTime.now());
    }

    // ---------------- GETTERS / SETTERS ----------------

    public String getPaymentCode() { return paymentCode; }

    public String getBusinessIBAN() { return businessIBAN; }

    public String getBusinessName() { return businessName; }

    public double getAmount() { return amount; }

    public String getDescription() { return description; }

    public boolean isPaid() { return paid; }

    public void setPaid(boolean paid) { this.paid = paid; }

    // --- Auto Pay Logic ---

    public boolean isAutoPayEnabled() { return autoPay; }

    public void setAutoPay(boolean autoPay) { this.autoPay = autoPay; }

    public String getAutoPayAccountIBAN() { return autoPayAccountIBAN; }

    public void setAutoPayAccountIBAN(String autoPayAccountIBAN) {
        this.autoPayAccountIBAN = autoPayAccountIBAN;
    }

    // --- Dates & Linking ---

    public String getIssueDate() { return issueDate; }

    public String getCustomerIBAN() { return customerIBAN; }

    public void setCustomerIBAN(String customerIBAN) {
        this.customerIBAN = customerIBAN;
    }

    // -------- ΜΗΝΙΑΙΟΣ ΛΟΓΑΡΙΑΣΜΟΣ --------

    public boolean isMonthly() {
        return subscriptionId != null;
    }

    public String getSubscriptionId() { return subscriptionId; }

    public void setSubscriptionId(String subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    // ---------------- TO STRING ----------------

    @Override
    public String toString() {
        return String.format(
                "Bill [%s] - %s: %.2f€ (%s)",
                paymentCode,
                businessName,
                amount,
                paid ? "PAID" : "UNPAID"
        );
    }
}