package backend.accounts;

import java.util.ArrayList;

public abstract class Account {

	long IBAN;	//εχει συγκεκριμενο αλγοριθμο 
	String User; //name...
	double balance;
	ArrayList<String> transactions; //na doume an theloume na einai ArrayList
	long account_id; //used to link account to user(s)
	
	
	public Account(long iBAN, String user, double balance, ArrayList<String> transactions) {
		super();
		this.IBAN = iBAN;
		this.User = user;
		this.balance = balance;
		this.transactions = transactions;
		this.account_id = account_id;
	}


	protected long getIBAN() {
		return IBAN;
	}


	protected void setIBAN(long iBAN) {
		//to IBAN exei GR + 2 check digits + 3 ψηφία κωδικό τράπεζας + 4 ψηφία κωδικός καταστήματος + 16 ψηφία αριθμός λογαριασμού
		IBAN = iBAN;
	}


	protected String getUser() {
		return User;
	}


	protected void setUser(String user) {
		User = user;
	}


	protected double getBalance() {
		return balance;
	}


	protected void setBalance(double balance) {
		this.balance = balance;
	}


	protected ArrayList<String> getTransactions() {
		return transactions;
	}


	protected void setTransactions(ArrayList<String> transactions) {
		this.transactions = transactions;
	}


	protected long getAccount_id() {
		return account_id;
	}


	protected void setAccount_id(long account_id) {
		this.account_id = account_id;
	}
	
	//protected void createAccount()
	
}
