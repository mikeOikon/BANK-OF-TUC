package backend.accounts;

import java.util.Stack;

import backend.Branch;
import backend.transactions.Transaction;

public class AccountBuilder {

    private String userID;
    private double balance;
    private Branch branch;
    private Stack<Transaction> transactions = new Stack<>();
    private double interest;
    private String IBAN;


    public AccountBuilder withUserID(String userID) {
        this.userID = userID;
        return this;
    }

    public AccountBuilder withBalance(double balance) {
        this.balance = balance;
        return this;
    }

    public AccountBuilder withBranch(Branch branch) {
        this.branch = branch;
        return this;
    }

    public AccountBuilder withTransactions(Stack<Transaction> transactions) {
        if (transactions != null)
            this.transactions = transactions;
        return this;
    }
    
    public AccountBuilder withInterest(double interest) {
		this.interest = interest;
		return this;
	}

    public String getUserID() { 
    	return userID; 
    }

    public double getBalance() { 
    	return balance; 
    	}

    public Branch getBranch() { 
    	return branch; 
    }
    
    public Stack<Transaction> getTransactions() {
    	return transactions; 
    }

	protected String getIBAN() {
		return IBAN;
	}

	protected void setIBAN(String iBAN) {
		IBAN = iBAN;
	}

	public double getInterest() {
		return interest;
	}

}
