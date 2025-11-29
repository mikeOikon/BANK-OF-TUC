package backend.accounts;

import java.util.Stack;

import backend.Branch;
import backend.transactions.Transaction;

public class FixedTermAccount extends PersonalAccount {

	public FixedTermAccount(String user, double balance, Stack<Transaction> transactions, Branch branch) {
		super(user, balance, transactions, branch);
		// TODO Auto-generated constructor stub
	}
	
}
