package backend.accounts;

import java.util.*;
import backend.Branch;
import backend.transactions.*;
import types.AccountType;

public abstract class Account {
    private boolean primary;
    private boolean frozen; 
    protected String IBAN;    
    protected String userID;
    protected double balance;
    protected Stack<Transaction> transactions; 
    private static long nextAccountId = 1;
    private long account_id;
    private static Set<String> usedAccounts = new HashSet<>(); 
    protected double interest; 
    private Branch branch;    
    
    public Account(String IBAN, String userID, double balance, Stack<Transaction> transactions, double interest, Branch branch) {
        this.IBAN = (IBAN != null && !IBAN.isEmpty()) ? IBAN : generateIBAN(branch); 
        this.interest = interest;
        this.userID = userID;
        this.balance = balance;
        this.transactions = (transactions != null) ? transactions : new Stack<>();
        this.account_id = nextAccountId++;
        this.branch = branch;
        this.frozen = false;
    }

    // --- Getters & Setters ---
    public Branch getBranch() { return branch; }
    public String getIBAN() { return IBAN; }
    public double getBalance() { return balance; }
    public void setBalance(double balance) { this.balance = balance; }
    public Stack<Transaction> getTransactions() { return transactions; }
    public boolean isFrozen() { return frozen; }
    public void setFrozen(boolean frozen) { this.frozen = frozen; }
    public abstract AccountType getAccountType();
    
    public boolean isPrimary() {
        return this.primary;
    }

	public void setPrimary(boolean primary) {
		this.primary = primary;
	}
	
	public void setInterest(double interest) {
		this.interest = interest;
	}
	
	public double getInterest() {
		return interest;
	}

    // --- Core Logic Methods ---

    public void deposit(double amount) {
        if (frozen) throw new IllegalStateException("Account is frozen.");
        if (amount <= 0) throw new IllegalArgumentException("Amount must be positive.");

        // Η DepositTransaction θα αυξήσει το balance
        Transaction tx = new DepositTransaction(amount, this);
        transactions.push(tx);
    }

    public boolean withdraw(double amount) {
        if (frozen) throw new IllegalStateException("Account is frozen.");
        if (amount <= 0) throw new IllegalArgumentException("Amount must be positive.");
        if (this.balance < amount) throw new IllegalArgumentException("Insufficient funds.");

        // Η WithdrawTransaction θα μειώσει το balance
        Transaction tx = new WithdrawTransaction(this, amount);
        transactions.push(tx);
        return true;
    }

    public boolean transferTo(Account target, double amount) {
        if (frozen) throw new IllegalStateException("Source account is frozen.");
        if (target == null) throw new IllegalArgumentException("Target account cannot be null.");
        if (target.isFrozen()) throw new IllegalStateException("Target account is frozen.");
        if (this.balance < amount) throw new IllegalArgumentException("Insufficient funds.");

        // Η TransferTransaction θα αλλάξει και τα δύο balances
        Transaction tx = new TransferTransaction(this, target, amount);
        this.transactions.push(tx);
        target.getTransactions().push(tx);
        return true;
    }

    // --- IBAN Generation (Helper Methods) ---
    private static String lettersToDigits(String input) {
        StringBuilder sb = new StringBuilder();
        for (char ch : input.toCharArray()) {
            if (Character.isLetter(ch)) {
                sb.append(Character.toUpperCase(ch) - 'A' + 10);
            } else sb.append(ch);
        }
        return sb.toString();
    }
    
    public static String generateIBAN(Branch branch) {
        Random random = new Random();
        String bban;
        do {
            bban = branch.getBankCode() + branch.getBranchCode() + String.format("%016d", Math.abs(random.nextLong()) % 1_0000_0000_0000_0000L);
        } while (usedAccounts.contains(bban));
        usedAccounts.add(bban);

        String numericString = lettersToDigits(bban + "GR00");
        int checkDigits = 98 - mod97(numericString);
        return "GR" + String.format("%02d", checkDigits) + bban;
    }

    private static int mod97(String numeric) {
        int remainder = 0;
        for (int i = 0; i < numeric.length(); i++) {
            remainder = (remainder * 10 + (numeric.charAt(i) - '0')) % 97;
        }
        return remainder;
    }
    
    public boolean supportsInterest() {
		return false;
	}
    
    public double getInterestRate() {
        return interest;
    }

    public String toString() {
        return String.format("%s | IBAN: %s | Balance: %.2f | %s", 
            getClass().getSimpleName(), IBAN, balance, (frozen ? "[FROZEN]" : "ACTIVE"));
    }
}