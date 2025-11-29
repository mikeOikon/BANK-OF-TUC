package backend.transactions;

import backend.accounts.Account;

public class TransferTransaction extends Transaction {

    public TransferTransaction(Account from, Account to, double amount) {
        super(
            TransactionType.TRANSFER,
            amount,
            from.getIBAN(),
            to.getIBAN()
        );
    }
}
