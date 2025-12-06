package backend.transactions.validation;

import backend.accounts.Account;

public class AmountValidationStrategy implements TransactionValidationStrategy {

    @Override
    public void validate(Account from, Account to, double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be > 0.");
        }
    }
}
