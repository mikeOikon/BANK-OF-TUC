package backend.transactions;

import backend.accounts.Account;
import types.TransactionType;

// Factory class for creating different types of transactions
public class TransactionFactory {

    // Deposit & Withdraw
    public static Transaction createTransaction(
            TransactionType type,
            Account account,
            double amount
    ) {
        return switch (type) {
            case DEPOSIT -> new DepositTransaction(account, amount);
            case WITHDRAW -> new WithdrawTransaction(account, amount);
            default -> throw new IllegalArgumentException(
                    "Use the second createTransaction() method for TRANSFER"
            );
        };
    }

    // Transfer
    public static Transaction createTransaction(
            Account from,
            Account to,
            double amount
    ) {
        return new TransferTransaction(from, to, amount);
    }
}
