package backend.accounts;

import java.util.ArrayList;

public abstract class BusinessAccount extends Account {

	public BusinessAccount(long iBAN, String user, double balance, ArrayList<String> transactions) {
		super(iBAN, user, balance, transactions);
		// TODO Auto-generated constructor stub
	}

}
