package backend.accounts;

import java.util.Stack;

import backend.Branch;
import backend.transactions.Transaction;
import types.AccountType;

public class AccountFactory {

	public static Account createAccount(
	        AccountType type,
	        String userID,
	        double balance,
	        Branch branch
	) {
	    Stack<Transaction> tx = new Stack<>();

	    return switch (type) {
	        case TRANSACTIONAL -> new TransactionalAccount(userID, balance, tx, branch);
	        case SAVINGS -> new SavingsAccount(userID, balance, tx, branch);
	        case FIXED -> new FixedTermAccount(userID, balance, tx, branch);
	        case BUSINESS -> new BusinessAccount(userID, balance, tx, branch);
	    };
	}

	
}
