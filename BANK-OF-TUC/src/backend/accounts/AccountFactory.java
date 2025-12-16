package backend.accounts;

import types.AccountType;

public class AccountFactory {

    public static Account createAccount(AccountType type, AccountBuilder builder) {

        switch (type) {

            case TRANSACTIONAL:
                return new TransactionalAccount(
                        builder.getUserID(),
                        builder.getBalance(),
                        builder.getTransactions(),
                        builder.getBranch()
                );

            case SAVINGS:
                return new SavingsAccount(
                        builder.getUserID(),
                        builder.getBalance(),
                        builder.getTransactions(),
                        builder.getBranch()
                );

            case FIXED:
                return new FixedTermAccount(
                        builder.getUserID(),
                        builder.getBalance(),
                        builder.getTransactions(),
                        builder.getBranch(), 0, 0
                );

            case BUSINESS:
                return new BusinessAccount(
                        builder.getUserID(),
                        builder.getBalance(),
                        builder.getTransactions(),
                        builder.getBranch()
                );

            default:
                throw new IllegalArgumentException("Invalid account type: " + type);
        }
    }
}
