package backend.accounts;

import java.util.Stack;

public class BusinessAccount extends Account {

	public BusinessAccount(String userID, double balance, Stack<Transaction> transactions, Branch branch) {
		super(userID, balance, transactions, branch);
		// TODO Auto-generated constructor stub
	}
	//πιο υψηλά τέλη διαχείρισης, περισσοτερες δυνατοτητες
}
