package backend.accounts;

import java.util.Stack;

import backend.Branch;
import backend.transactions.Transaction;
import backend.transactions.WithdrawTransaction;
import types.AccountType;

public class TransactionalAccount extends PersonalAccount {

    public TransactionalAccount(String IBAN, String userID, double balance, Stack<Transaction> transactions, double interest, Branch branch) {
        super(IBAN, userID, balance, transactions, interest, branch);
    }

    @Override
    public AccountType getAccountType() {
        return AccountType.TRANSACTIONAL;
    }

    @Override
    public String toString() {
        return String.format(
                "%s | Type: %s | IBAN: %s | Balance: %.2f | Branch: %s",
                this.getClass().getSimpleName(),
                getAccountType(),
                this.getIBAN(),
                this.getBalance(),
                (getBranch() != null ? getBranch().getBranchCode() : "N/A")
        );
    }
    
}

	

