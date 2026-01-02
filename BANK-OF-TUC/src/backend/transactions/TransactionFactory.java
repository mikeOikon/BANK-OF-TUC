package backend.transactions;

import backend.accounts.Account;
import backend.support.Bill;
import types.TransactionType;

public class TransactionFactory {

    public static Transaction createTransaction(TransactionType type, Account account, double amount) {
        return switch (type) {
            case DEPOSIT -> new DepositTransaction(amount, account);
            case WITHDRAW -> new WithdrawTransaction(account, amount);
            default -> throw new IllegalArgumentException(
                    "Use other factory methods for TRANSFER or special transactions"
            );
        };
    }

    public static Transaction createTransaction(Account from, Account to, double amount) {
        return new TransferTransaction(from, to, amount);
    }

    // --- Νέες συναλλαγές ---
    public static Transaction createBillPaymentTransaction(Account from, Bill bill) {
        return new BillPaymentTransaction(from, bill);
    }

    public static Transaction createAutoBillPaymentTransaction(Account from, Bill bill) {
        return new AutoBillPaymentTransaction(from, bill);
    }

    public static Transaction createInterestTransaction(Account target, double amount) {
        return new InterestTransaction(target, amount);
    }

    public static Transaction createFeeTransaction(Account target, double amount, String description) {
        return new FeeTransaction(target, amount, description);
    }
}
