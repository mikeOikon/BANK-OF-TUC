package backend.accounts;

import java.util.ArrayList;

public class BusinessPayrollAccount extends BusinessAccount {

	public BusinessPayrollAccount(long iBAN, String user, double balance, ArrayList<String> transactions) {
		super(iBAN, user, balance, transactions);
		// TODO Auto-generated constructor stub
	}

	//για να πληρωνει μισθούς το προσωπικό της επιχείρησης
}
