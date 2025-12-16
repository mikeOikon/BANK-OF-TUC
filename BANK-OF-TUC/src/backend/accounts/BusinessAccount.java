package backend.accounts;

import java.util.Stack;

import backend.Branch;
import backend.transactions.Transaction;
import backend.transactions.WithdrawTransaction;
import types.AccountType;

public class BusinessAccount extends Account {

    private double managementFee;    //τέλη διαχείρισης

    public BusinessAccount(
    		String IBAN,
            String userID,
            double balance,
            Stack<Transaction> transactions,
            double interest,
            Branch branch
    ) {
    	super(IBAN, userID, balance, transactions, interest, branch);
        this.managementFee = 10.0;
    }

    @Override
    public AccountType getAccountType() {
        return AccountType.BUSINESS;
    }

    public double getManagementFee() {
        return managementFee;
    }

    public void applyMonthlyFee() {
        this.balance -= managementFee;
    }

    @Override
    public String toString() {
        return String.format(
                "%s | Type: %s | Business: %s | IBAN: %s | Balance: %.2f | Fee: %.2f | Branch: %s",
                this.getClass().getSimpleName(),
                getAccountType(),
                this.getIBAN(),
                this.getBalance(),
                managementFee,
                (getBranch() != null ? getBranch().getBranchCode() : "N/A")
        );
    }
    
    @Override
    public void withdraw(double amount) {
        double total = amount + managementFee;

        if (balance < total)
            throw new IllegalArgumentException("Insufficient funds for withdrawal + fee.");

        balance -= total;

        transactions.push(new WithdrawTransaction(this, amount));
    }
    
    @Override
    public void transferTo(Account target, double amount) {
    	double total = amount + managementFee;
    	
    	if (balance < total)
            throw new IllegalArgumentException("Insufficient funds for transaction + fee.");
    	
    	balance -= managementFee;
        super.transferTo(target, amount);
    }
    
    @Override
    public void deposit(double amount) {

        super.deposit(amount);
    }
}
