package backend.accounts;

import java.util.Stack;

import backend.Branch;
import backend.transactions.Transaction;

public class BusinessAccount extends Account {

	public BusinessAccount(String userID, double balance, Stack<Transaction> transactions, Branch branch) {
		super(userID, balance, transactions, branch);
		// TODO Auto-generated constructor stub
	}
	//πιο υψηλά τέλη διαχείρισης, περισσοτερες δυνατοτητες
	
}
