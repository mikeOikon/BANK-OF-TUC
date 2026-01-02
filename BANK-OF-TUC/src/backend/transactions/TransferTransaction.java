package backend.transactions;

import backend.accounts.Account;
import types.TransactionType;

public class TransferTransaction extends Transaction {

    public TransferTransaction(Account from, Account to, double amount) {
        super(
                TransactionType.TRANSFER,
                amount,
                from.getIBAN(),
                to.getIBAN(),
                "Transfer from " + from.getIBAN() + " to " + to.getIBAN()
        );
        execute(from, to, amount);
    }
    
    private void execute(Account from, Account to, double amount) {
        // Αφαίρεση από τον αποστολέα
        from.setBalance(from.getBalance() - amount);
        
        // Πρόσθεση στον παραλήπτη
        to.setBalance(to.getBalance() + amount);
    }
}
