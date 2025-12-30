package backend.support;

import backend.BankSystem;
import backend.accounts.Account;

import java.util.List;

/**
 * Διαχειριστής Αυτόματων Πληρωμών
 */
public class AutoPayManager {

    private final BankSystem bankSystem;

    public AutoPayManager(BankSystem bankSystem) {
        this.bankSystem = bankSystem;
    }

    /**
     * Εκτελεί όλες τις αυτόματες πληρωμές που είναι ενεργοποιημένες
     * (π.χ. μηνιαίοι λογαριασμοί)
     */
    public void processAutoPayments() {
        List<Bill> bills = bankSystem.getAllBills();

        for (Bill bill : bills) {

            // Πληρώνουμε ΜΟΝΟ αν:
            // - δεν έχει πληρωθεί
            // - έχει ενεργό auto-pay
            if (bill.isPaid() || !bill.isAutoPayEnabled()) {
                continue;
            }

            if (bill.getCustomerIBAN() == null) {
                continue;
            }

            Account sourceAccount =
                    bankSystem.getAccountbyNumber(bill.getCustomerIBAN());

            if (sourceAccount == null) {
                continue;
            }

            if (sourceAccount.isFrozen()) {
                System.out.println("AutoPay skipped (frozen): " + bill.getPaymentCode());
                continue;
            }

            if (sourceAccount.getBalance() < bill.getAmount()) {
                System.out.println("AutoPay insufficient funds: " + bill.getPaymentCode());
                continue;
            }

            try {
                // Χρέωση πελάτη
                sourceAccount.withdraw(bill.getAmount());

                // Πίστωση επιχείρησης
                Account businessAcc =
                        bankSystem.getAccountbyNumber(bill.getBusinessIBAN());

                if (businessAcc != null) {
                    businessAcc.deposit(bill.getAmount());
                }

                bill.setPaid(true);

                System.out.println("AutoPay success: " + bill.getPaymentCode());

            } catch (Exception e) {
                System.err.println(
                        "AutoPay failed for " + bill.getPaymentCode() +
                        ": " + e.getMessage()
                );
            }
        }

        bankSystem.dao.save(bankSystem);
    }
}
