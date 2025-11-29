package backend.transactions;

import backend.accounts.Account;

public class DepositTransaction extends Transaction {

    public DepositTransaction(Account targetAccount, double amount) {
        super(
            TransactionType.DEPOSIT,
            amount,
            null,                         // deposit has no "from"
            targetAccount.getIBAN()
        );
    }
}
