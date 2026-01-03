package backend.support;

import backend.BankSystem;
import backend.accounts.Account;
import types.TransactionType;

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

            if (bill.getAutoPayAccountIBAN() == null) {
                continue;
            }

            Account sourceAccount =
                    bankSystem.getAccountbyNumber(bill.getAutoPayAccountIBAN());

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
            
            Account busAccount = bankSystem.getAccountbyNumber(bill.getBusinessIBAN());

            try {
                // Χρέωση πελάτη
            	bankSystem.transaction(TransactionType.AUTO_BILL_PAYMENT, sourceAccount, busAccount, bill.getAmount());


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
