package backend.transactions.validation;

import backend.accounts.Account;

public class IbanValidationStrategy implements TransactionValidationStrategy {

    @Override
    public void validate(Account from, Account to, double amount) {
        if (from != null && !from.getIBAN().startsWith("GR"))
            throw new IllegalArgumentException("Invalid IBAN (sender)");
        if (to != null && !to.getIBAN().startsWith("GR"))
            throw new IllegalArgumentException("Invalid IBAN (receiver)");
    }
}
