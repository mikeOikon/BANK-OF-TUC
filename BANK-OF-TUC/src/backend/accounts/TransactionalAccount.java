package backend.accounts;

import java.util.Stack;

import backend.Branch;
import backend.transactions.Transaction;

public class TransactionalAccount extends PersonalAccount {

	public TransactionalAccount(String userID, double balance, Stack<Transaction> transactions, Branch branch) {
		super(userID, balance, transactions, branch);
		// TODO Auto-generated constructor stub
	}
	
	public String getAccountType() {
		return "Transactional Account";
	}
	

	
}
