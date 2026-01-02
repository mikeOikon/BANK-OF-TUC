package backend.transactions;

import backend.accounts.Account;
import backend.BankSystem;
import types.TransactionType;

public class FeeTransaction extends Transaction {

    public FeeTransaction(Account targetAccount, double feeAmount, String description) {
        super(
                TransactionType.FEE,
                feeAmount,
                targetAccount.getIBAN(),
                null,
                description
        );
        execute(targetAccount, feeAmount);
    }

    private void execute(Account target, double feeAmount) {
        target.setBalance(target.getBalance() - feeAmount);

        BankSystem.getInstance().dao.save(BankSystem.getInstance());
    }
}
