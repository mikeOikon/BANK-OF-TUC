package backend.accounts;

import types.AccountType;

public class AccountFactory {

    public static Account createAccount(AccountType type, AccountBuilder builder) {

        switch (type) {

            case TRANSACTIONAL:
                return new TransactionalAccount(
                		builder.getIBAN(),
                        builder.getUserID(),
                        builder.getBalance(),
                        builder.getTransactions(),
                        builder.getInterest(),
                        builder.getBranch()
                );

            case SAVINGS:
                return new SavingsAccount(
                		builder.getIBAN(),
                        builder.getUserID(),
                        builder.getBalance(),
                        builder.getTransactions(),
                        builder.getInterest(),
                        builder.getBranch()
                );

            case FIXED:
                return new FixedTermAccount(
                		builder.getIBAN(),
                        builder.getUserID(),
                        builder.getBalance(),
                        builder.getTransactions(),
                        builder.getInterest(),
                        builder.getBranch(), 0
                );

            case BUSINESS:
                return new BusinessAccount(
                		builder.getIBAN(),
                        builder.getUserID(),
                        builder.getBalance(),
                        builder.getTransactions(),
                        builder.getInterest(),
                        builder.getBranch()
                );

            default:
                throw new IllegalArgumentException("Invalid account type: " + type);
        }
    }
}
