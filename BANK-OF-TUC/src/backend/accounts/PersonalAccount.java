package backend.accounts;

import java.util.ArrayList;
import java.util.Stack;

import backend.Branch;
import backend.transactions.Transaction;

public abstract class PersonalAccount extends Account {

	public PersonalAccount(String IBAN, String userID, double balance, Stack<Transaction> transactions, double interest, Branch branch) {
		super(IBAN, userID, balance, transactions, interest, branch);
	}
	
	
}
