package backend.transactions.validation;

import backend.accounts.Account;

public class BalanceValidationStrategy implements TransactionValidationStrategy {

    @Override
    public void validate(Account from, Account to, double amount) {
        if (from != null && from.getBalance() < amount) {
            throw new IllegalArgumentException("Insufficient funds.");
        }
    }
}
