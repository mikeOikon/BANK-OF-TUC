package backend.transactions;

import backend.accounts.Account;
import types.TransactionType;

public class DepositTransaction extends Transaction {

    public DepositTransaction(double amount, Account targetAccount) {
        super(
                TransactionType.DEPOSIT,
                amount,
                null,                           // fromAccountIban = null για deposit
                targetAccount.getIBAN(),        // toAccountIban
                "Deposit"                       // description
        );
        execute(targetAccount, amount);
    }

    private void execute(Account targetAccount, double amount) {
        targetAccount.setBalance(targetAccount.getBalance() + amount);
    }
}
