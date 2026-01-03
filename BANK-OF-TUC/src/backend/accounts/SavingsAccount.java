package backend.accounts;

import java.time.LocalDate;
import java.util.Stack;
import backend.Branch;
import backend.transactions.Transaction;
import backend.transactions.WithdrawTransaction;
import types.AccountType;
import types.TransactionType;

public class SavingsAccount extends PersonalAccount {
	//na dw
	private static final double managementFee = 2.0; // σταθερό τέλος διαχείρισης ανά ανάληψη
    
	public SavingsAccount(String IBAN, String userID, double balance, Stack<Transaction> transactions, double interest, Branch branch) {
        super(IBAN, userID, balance, transactions, interest, branch);
    }

    @Override
    public AccountType getAccountType() {
        return AccountType.SAVINGS;
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
    
    
    private double getTodayTransferTotal() {
        LocalDate today = backend.BankSystem.getInstance().getSimulatedToday();
        double total = 0;

        for (Transaction t : getTransactions()) {
            if (t.getType() == TransactionType.TRANSFER) {

                // δες αν η συναλλαγή έγινε σήμερα
                if (t.getTimestamp().toLocalDate().isEqual(today)) {

                    // δες αν ΕΓΩ ήμουν η πλευρά που έκανε το transfer
                    if (t.getFromAccountIban() != null &&
                        t.getFromAccountIban().equals(this.getIBAN())) {

                        total += t.getAmount();
                    }
                }
            }
        }
        return total;
    }

    private double getTodayDepositTotal() {
        LocalDate today = backend.BankSystem.getInstance().getSimulatedToday();
        double total = 0;

        for (Transaction t : getTransactions()) {
            if (t.getType() == TransactionType.DEPOSIT) {

                if (t.getTimestamp().toLocalDate().isEqual(today)) {

                    // Only count deposits TO this account
                    if (t.getToAccountIban() != null &&
                        t.getToAccountIban().equals(this.getIBAN())) {

                        total += t.getAmount();
                    }
                }
            }
        }
        return total;
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
