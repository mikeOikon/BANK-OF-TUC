package backend.accounts;

import java.util.Stack;

import backend.BankSystem;
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
                "%s | Type: %s | IBAN: %s | Balance: %.2f | Fee: %.2f | Branch: %s",
                this.getClass().getSimpleName(),
                getAccountType().name(),      // <-- enum σε String
                this.getIBAN(),
                this.getBalance(),
                managementFee,
                (getBranch() != null ? getBranch().getBranchCode() : "N/A")
        );
    }
    
    @Override
    public boolean withdraw(double amount) {
        double total = amount + managementFee;

        if (balance < total)
            throw new IllegalArgumentException("Insufficient funds for withdrawal + fee.");
        
        if (isFrozen())
			throw new IllegalStateException("Account is frozen.");
        // Μειώνουμε πρώτα balance για το fee
        balance -= managementFee;

        // Κάνουμε withdrawal μόνο το πραγματικό amount
        super.withdraw(amount);
        
        double curBalance = BankSystem.getInstance().getBankAccount().getBalance();
        BankSystem.getInstance().getBankAccount().setBalance(curBalance + managementFee); //τα τέλη πηγαίνουν στην τράπεζα

        return true;
    }

    
    @Override
    public boolean transferTo(Account target, double amount) {
        double total = amount + managementFee;
        
        if (balance < total)
            throw new IllegalArgumentException("Insufficient funds for transaction + fee.");
        
        if (target.isFrozen())
			throw new IllegalStateException("Account is frozen.");
        
        balance -= managementFee;
        
        super.transferTo(target, amount);
        
        double curBalance = BankSystem.getInstance().getBankAccount().getBalance();
        BankSystem.getInstance().getBankAccount().setBalance(curBalance + managementFee); //τα τέλη πηγαίνουν στην τράπεζα
        
        return true;
    }

    
    @Override
    public void deposit(double amount) {

        
        if (isFrozen())
			throw new IllegalStateException("Account is frozen.");
        
        super.deposit(amount);
    }
    

}
