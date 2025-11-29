package backend.accounts;

import java.util.ArrayList;
import java.util.Stack;

import backend.Branch;
import backend.transactions.Transaction;

public abstract class PersonalAccount extends Account {

	public PersonalAccount(String userID, double balance, Stack<Transaction> transactions, Branch branch) {
		super(userID, balance, transactions, branch);
		// TODO Auto-generated constructor stub
	}
	
}
