package backend.support;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Bill {

    protected String paymentCode;
    private String businessIBAN;
    private String businessName;

    private double amount;
    private String description;
    private String issueDate;

    protected boolean paid;

    // --- Auto Pay ---
    private boolean autoPay;
    private String autoPayAccountIBAN; // ΜΟΝΟ αν ενεργοποιηθεί

    // --- Monthly ---
    private String subscriptionId; // null = όχι μηνιαίος

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
        this.description = "bill payment";

        this.paid = false;
        this.autoPay = false;
        this.autoPayAccountIBAN = null;
        this.subscriptionId = null;

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        this.issueDate = dtf.format(LocalDateTime.now());
    }

    // -------- GETTERS / SETTERS --------

    public String getPaymentCode() { return paymentCode; }
    public String getBusinessIBAN() { return businessIBAN; }
    public String getBusinessName() { return businessName; }
    public double getAmount() { return amount; }
    public boolean isPaid() { return paid; }
    public void setPaid(boolean paid) { this.paid = paid; }

    // --- AutoPay ---
    public boolean isAutoPayEnabled() { return autoPay; }

    public void enableAutoPay(String iban) {
        this.autoPay = true;
        this.autoPayAccountIBAN = iban;
    }

    public void disableAutoPay() {
        this.autoPay = false;
        this.autoPayAccountIBAN = null;
    }

    public String getAutoPayAccountIBAN() {
        return autoPayAccountIBAN;
    }

    // --- Monthly ---
    public boolean isMonthly() {
        return subscriptionId != null;
    }

    public String getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(String subscriptionId) {
        this.subscriptionId = subscriptionId;
    }
}
