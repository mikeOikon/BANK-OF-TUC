package backend.transactions;

import backend.accounts.Account;
import types.TransactionType;

public class TransactionBuilder {

    private TransactionType type;
    private Account from;
    private Account to;
    private double amount;


    public TransactionBuilder setType(TransactionType type) {
        this.type = type;
        return this;
    }

    public TransactionBuilder setFrom(Account from) {
        this.from = from;
        return this;
    }

    public TransactionBuilder setTo(Account to) {
        this.to = to;
        return this;
    }

    public TransactionBuilder setAmount(double amount) {
        this.amount = amount;
        return this;
    }

    public Transaction build() {

        if (type == null)
            throw new IllegalStateException("Transaction type is missing.");

        if (amount <= 0)
            throw new IllegalStateException("Amount must be > 0.");

        switch (type) {

            case DEPOSIT:
                if (to == null)
                    throw new IllegalStateException("Deposit needs a target account.");
                return TransactionFactory.createTransaction(
                        TransactionType.DEPOSIT,
                        to,
                        amount
                );

            case WITHDRAW:
                if (from == null)
                    throw new IllegalStateException("Withdraw needs a source account.");
                return TransactionFactory.createTransaction(
                        TransactionType.WITHDRAW,
                        from,
                        amount
                );

            case TRANSFER:
                if (from == null || to == null)
                    throw new IllegalStateException("Transfer needs both source and target accounts.");
                return TransactionFactory.createTransaction(from, to, amount);

            default:
                throw new UnsupportedOperationException("Unsupported transaction type.");
        }
    }
}
