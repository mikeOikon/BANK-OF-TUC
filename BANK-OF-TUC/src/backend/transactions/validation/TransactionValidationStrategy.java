package backend.transactions.validation;

import backend.accounts.Account;

public interface TransactionValidationStrategy {
    void validate(Account from, Account to, double amount);
}

