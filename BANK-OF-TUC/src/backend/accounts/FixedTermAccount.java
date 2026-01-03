package backend.accounts;

import java.util.Stack;
import backend.Branch;
import backend.transactions.Transaction;
import backend.transactions.WithdrawTransaction;
import java.time.LocalDate;
import types.AccountType;

public class FixedTermAccount extends PersonalAccount {

    private int termMonths;
    // ΑΦΑΙΡΕΘΗΚΕ ΤΟ transient ώστε να αποθηκεύονται στο JSON
    private LocalDate startDate;     
    private LocalDate maturityDate;  

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
        this.startDate = backend.BankSystem.getInstance().getSimulatedToday();
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
        // Προσθήκη ελέγχου null για ασφάλεια αν το αρχείο JSON είναι κατεστραμμένο
        if (maturityDate == null) {
            return false;
        }
        return backend.BankSystem.getInstance().getSimulatedToday().isAfter(maturityDate) || backend.BankSystem.getInstance().getSimulatedToday().isEqual(maturityDate);
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
                (maturityDate != null ? maturityDate : "N/A")
        );
    }
    
    @Override
    public boolean supportsInterest() {
		return true;
	}
    
    @Override
    public double getInterestRate() {
        return interest;
    }
}