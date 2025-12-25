package backend.accounts;

import java.util.Stack;

import backend.Branch;
import backend.transactions.Transaction;
import backend.transactions.WithdrawTransaction;

import java.time.LocalDate;
import types.AccountType;

public class FixedTermAccount extends PersonalAccount {

    private int termMonths;          // διάρκεια προθεσμίας
    private transient LocalDate startDate;     // ημερομηνία δημιουργίας
    private transient LocalDate maturityDate;  // ημερομηνία λήξης

    public FixedTermAccount(
            String IBAN,
            String userID,
            double balance,
            Stack<Transaction> transactions,
            double interest, 
            Branch branch,
            int termMonths
    ) {
        super(IBAN, userID, balance, transactions, interest, branch);
        this.termMonths = termMonths;
        this.startDate = LocalDate.now();
        this.maturityDate = startDate.plusMonths(termMonths);
    }

    @Override
    public AccountType getAccountType() {
        return AccountType.FIXED;
    }   

    public int getTermMonths() {
        return termMonths;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getMaturityDate() {
        return maturityDate;
    }

    public boolean isMatured() {
        return LocalDate.now().isAfter(maturityDate) || LocalDate.now().isEqual(maturityDate);
    }

    @Override
    public String toString() {
        return String.format(
                "%s | Type: %s | IBAN: %s | Balance: %.2f | Interest: %.2f%% | Maturity: %s",
                this.getClass().getSimpleName(),
                getAccountType(),
                this.getIBAN(),
                this.getBalance(),
                this.getInterest(),
                maturityDate
        );
    }
    
    @Override
    public boolean withdraw(double amount) {
    	
    	if (!isMatured()) {
			throw new IllegalStateException("Cannot withdraw from Fixed Term Account before maturity date.");
		}
        if (balance < amount)
            throw new IllegalArgumentException("Insufficient funds for withdrawal + fee.");

        balance -= amount;

        transactions.push(new WithdrawTransaction(this, amount));
        
        return true;
    }
    
    @Override
    public void deposit(double amount) {

    	throw new IllegalStateException("Cannot deposit on a fixed-term account.");
    }
    
    @Override
    public boolean transferTo(Account target, double amount) {

        throw new IllegalArgumentException("You cannot transfer from a Fixed Term Account.");
    }
}
