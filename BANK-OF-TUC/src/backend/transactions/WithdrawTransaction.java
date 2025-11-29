package backend.transactions;

import backend.accounts.Account;

public class WithdrawTransaction extends Transaction {

    public WithdrawTransaction(Account sourceAccount, double amount) {
        super(
            TransactionType.WITHDRAW,
            amount,
            sourceAccount.getIBAN(),      // withdraw has "from"
            null                          // no "to"
        );
    }
}
