package backend.support;

import backend.BankSystem;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class MonthlySubscription {

    private final String subscriptionId;
    private final String businessIBAN;
    private final String businessName;
    private final double monthlyAmount;
    private final String description;

    // Auto-pay (αν ο χρήστης το ενεργοποιήσει)
    private String autoPayAccountIBAN;

    private LocalDate lastIssuedMonth;

    public MonthlySubscription(
            String businessIBAN,
            String businessName,
            double monthlyAmount,
            String description
    ) {
        this.subscriptionId = UUID.randomUUID().toString();
        this.businessIBAN = businessIBAN;
        this.businessName = businessName;
        this.monthlyAmount = monthlyAmount;
        this.description = description;

        this.autoPayAccountIBAN = null;
        this.lastIssuedMonth = null;
    }

    public void generateMonthlyBill(BankSystem bankSystem) {
        LocalDate currentMonth = bankSystem.getSimulatedToday().withDayOfMonth(1);

        if (lastIssuedMonth != null && lastIssuedMonth.equals(currentMonth)) {
            return;
        }

        String paymentCode = "RF" + UUID.randomUUID()
                .toString()
                .replace("-", "")
                .substring(0, 12)
                .toUpperCase();

        String fullDesc = description + " (" +
                currentMonth.format(DateTimeFormatter.ofPattern("MM/yyyy")) + ")";

        Bill bill = new Bill(
                paymentCode,
                businessIBAN,
                businessName,
                monthlyAmount,
                fullDesc
        );

        bill.setSubscriptionId(subscriptionId);

        // 👉 ΜΟΝΟ αν ο χρήστης έχει ενεργοποιήσει auto-pay
        if (autoPayAccountIBAN != null) {
            bill.enableAutoPay(autoPayAccountIBAN);
        }

        bankSystem.addBill(bill);
        lastIssuedMonth = currentMonth;

        System.out.println("[Subscription] New bill issued: " + paymentCode);
    }

    // --- AutoPay ---
    public void enableAutoPay(String iban) {
        this.autoPayAccountIBAN = iban;
    }

    public void disableAutoPay() {
        this.autoPayAccountIBAN = null;
    }

    public String getSubscriptionId() {
        return subscriptionId;
    }

    public String getAutoPayAccountIBAN() {
        return autoPayAccountIBAN;
    }
}
