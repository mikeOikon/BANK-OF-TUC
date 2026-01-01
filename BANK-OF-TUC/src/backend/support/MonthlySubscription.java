package backend.support;

import backend.BankSystem;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * Αντιπροσωπεύει μια μηνιαία υπηρεσία (π.χ. συνδρομή γυμναστηρίου, τηλεφωνία).
 * Παράγει αυτόματα αντικείμενα Bill κάθε μήνα.
 */
public class MonthlySubscription {

    private final String subscriptionId;
    private final String businessIBAN;
    private final String businessName;
    private final double monthlyAmount;
    private final String description;
    private String accountIBAN;

    // Στοιχεία Αυτόματης Πληρωμής (Auto-Pay)
    // Αρχικά είναι null μέχρι ο πελάτης να ενεργοποιήσει την πάγια εντολή
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
        
        this.autoPayAccountIBAN = null; // Default: Χειροκίνητη πληρωμή
        this.lastIssuedMonth = null;
        this.accountIBAN = null;
    }

    /**
     * Δημιουργεί ένα νέο Bill για τον τρέχοντα μήνα.
     * Αν υπάρχει ενεργό Auto-Pay, το Bill θα φέρει τα στοιχεία χρέωσης.
     */
    public void generateMonthlyBill(BankSystem bankSystem) {
        // Χρησιμοποιούμε την ημερομηνία του συστήματος (Simulated ή Real)
        LocalDate currentMonth = bankSystem.getSimulatedToday().withDayOfMonth(1);

        // Έλεγχος αν έχει ήδη εκδοθεί λογαριασμός για αυτόν τον μήνα
        if (lastIssuedMonth != null && lastIssuedMonth.equals(currentMonth)) {
            return;
        }

        String paymentCode = generatePaymentCode();
        String fullDescription = description + " (" + 
                currentMonth.format(DateTimeFormatter.ofPattern("MM/yyyy")) + ")";

        // Δημιουργία του αντικειμένου Bill
        Bill bill = new Bill(
                paymentCode,
                businessIBAN,
                businessName,
                monthlyAmount,
                fullDescription
        );

        // Σύνδεση του Bill με τη συγκεκριμένη συνδρομή
        bill.setSubscriptionId(this.subscriptionId);

        // ✅ ΕΛΕΓΧΟΣ AUTO-PAY:
        // Αν ο πελάτης έχει συνδέσει λογαριασμό στη συνδρομή, τον περνάμε στο Bill
        if (this.autoPayAccountIBAN != null) {
            bill.setAutoPay(true);
            bill.setAutoPayAccountIBAN(this.autoPayAccountIBAN);
        }

        // Προσθήκη του λογαριασμού στο κεντρικό σύστημα
        bankSystem.addBill(bill);
        
        this.lastIssuedMonth = currentMonth;

        System.out.printf("[Subscription] New bill %s generated for %s. Auto-Pay: %s%n", 
                paymentCode, businessName, (autoPayAccountIBAN != null ? "YES" : "NO"));
    }

    /**
     * Ενεργοποιεί την πάγια εντολή για αυτή τη συνδρομή.
     * Καλούνται από το BillPaymentTab όταν ο πελάτης επιλέξει "Auto-Pay".
     */
    public void enableAutoPay(String accountIBAN) {
        this.autoPayAccountIBAN = accountIBAN;
    }

    /**
     * Απενεργοποιεί την πάγια εντολή.
     */
    public void disableAutoPay() {
        this.autoPayAccountIBAN = null;
    }

    private String generatePaymentCode() {
        return "RF" + UUID.randomUUID()
                .toString()
                .replace("-", "")
                .substring(0, 12)
                .toUpperCase();
    }

    // --- Getters ---

    public String getSubscriptionId() { return subscriptionId; }
    public String getBusinessIBAN() { return businessIBAN; }
    public String getBusinessName() { return businessName; }
    public double getMonthlyAmount() { return monthlyAmount; }
    public String getDescription() { return description; }
    public String getAutoPayAccountIBAN() { return autoPayAccountIBAN; }
    public boolean isAutoPayEnabled() { return autoPayAccountIBAN != null; }
}