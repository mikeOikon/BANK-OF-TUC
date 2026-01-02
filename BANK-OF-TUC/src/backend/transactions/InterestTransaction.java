package backend.transactions;

import backend.accounts.Account;
import backend.BankSystem;
import types.TransactionType;

public class InterestTransaction extends Transaction {

    public InterestTransaction(Account targetAccount, double interestAmount) {
        super(
                TransactionType.INTEREST,
                interestAmount,
                null,
                targetAccount.getIBAN(),
                "Interest Credit"
        );
        execute(targetAccount, interestAmount);
    }

    private void execute(Account target, double interestAmount) {
        target.setBalance(target.getBalance() + interestAmount);

        BankSystem.getInstance().dao.save(BankSystem.getInstance());
    }
}
