package backend.accounts;

import java.util.Stack;

import backend.Branch;
import backend.transactions.Transaction;
import backend.transactions.WithdrawTransaction;
import types.AccountType;

public class TransactionalAccount extends PersonalAccount {

    public TransactionalAccount(String userID, double balance, Stack<Transaction> transactions, Branch branch) {
        super(userID, balance, transactions, branch);
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
    
    @Override
    public void withdraw(double amount) {;

        if (balance < amount)
            throw new IllegalArgumentException("Insufficient funds for withdrawal + fee.");

        balance -= amount;

        super.withdraw(amount);
    }
    
    @Override
    public void transferTo(Account target, double amount) {

    	if (balance < amount)
            throw new IllegalArgumentException("Insufficient funds for withdrawal + fee.");

        super.transferTo(target, amount);
    }
    
    
    @Override
    public void deposit(double amount) {

        super.deposit(amount);
    }

}

	

