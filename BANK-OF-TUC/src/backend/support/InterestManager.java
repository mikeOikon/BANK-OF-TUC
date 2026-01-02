package backend.support;

import backend.BankSystem;
import backend.accounts.Account;

/**
 * Manager που υπολογίζει και πιστώνει τον τόκο στους λογαριασμούς που τον υποστηρίζουν.
 */
public class InterestManager {

    private final BankSystem bankSystem;

    public InterestManager(BankSystem bankSystem) {
        this.bankSystem = bankSystem;
    }

    /**
     * Εφαρμογή μηνιαίου τόκου σε όλους τους κατάλληλους λογαριασμούς.
     * Ο τόκος υπολογίζεται ως monthlyRate = annualRate / 12
     */
    public void applyMonthlyInterest() {

        for (Account acc : bankSystem.getAllAccounts()) {

            // Αν ο λογαριασμός δεν υποστηρίζει τόκο ή είναι παγωμένος → skip
            if (!acc.supportsInterest() || acc.isFrozen()) continue;

            // Μηνιαίος τόκος
            double monthlyRate = acc.getInterestRate() / 12.0;
            double interest = acc.getBalance() * monthlyRate;

            if (interest <= 0) continue;

            // Πίστωση στον λογαριασμό
            acc.deposit(interest);

            System.out.printf(
                    "[InterestManager] %s: +%.2f€ τόκος (rate=%.2f%%)%n",
                    acc.getIBAN(),
                    interest,
                    monthlyRate * 100
            );
        }

        // Αποθήκευση της τράπεζας
        bankSystem.dao.save(bankSystem);
    }
}
