package backend.transactions;

import backend.accounts.Account;
import types.TransactionType;

public class WithdrawTransaction extends Transaction {

    public WithdrawTransaction(Account sourceAccount, double amount) {
        super(
                TransactionType.WITHDRAW,
                amount,
                sourceAccount.getIBAN(),      // withdraw has "from"
                null,                          // no "to"
                "Withdraw from " + sourceAccount.getIBAN()
        );
        execute(sourceAccount, amount);
    }
    
    private void execute(Account sourceAccount, double amount) {
		sourceAccount.setBalance(sourceAccount.getBalance() - amount);
	}
}
