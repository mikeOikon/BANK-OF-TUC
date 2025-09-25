package backend;

public class TransactionalAccount extends Account {
	
	//ola einai auto generated
	private String accountNumber;
	private double balance;
	private String accountType; // e.g., "Checking", "Savings"
	private String currency; // e.g., "USD", "EUR"
	private String accountHolderName;
	private String accountHolderID; // ID of the customer who owns the account
	
	// Constructor
	public TransactionalAccount(String accountNumber, double initialBalance, String accountType, String currency, String accountHolderName, String accountHolderID) {
		this.accountNumber = accountNumber;
		this.balance = initialBalance;
		this.accountType = accountType;
		this.currency = currency;
		this.accountHolderName = accountHolderName;
		this.accountHolderID = accountHolderID;
	}
	
	// Getters and Setters
	public String getAccountNumber() {
		return accountNumber;
	}
	
	public double getBalance() {
		return balance;
	}
	
	public void setBalance(double balance) {
		this.balance = balance;
	}
	
	public String getAccountType() {
		return accountType;
	}
	
	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}
	
	public String getCurrency() {
		return currency;
	}
	
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	
	public String getAccountHolderName() {
		return accountHolderName;
	}
	
	public void setAccountHolderName(String accountHolderName) {
		this.accountHolderName = accountHolderName;
	}
	
	public String getAccountHolderID() {
		return accountHolderID;
	}
	
	public void setAccountHolderID(String accountHolderID) {
		this.accountHolderID = accountHolderID;
	}
	
	// Method to deposit money
	public void deposit(double amount) {
		if (amount > 0) {
			balance += amount;
			System.out.println("Deposited: " + amount + " " + currency);
		} else {
			System.out.println("Deposit amount must be positive.");
		}
	}
	
	// Method to withdraw money
	public boolean withdraw(double amount) {
		if (amount > 0 && amount <= balance) {
			balance -= amount;
			System.out.println("Withdrew: " + amount + " " + currency);
			return true;
		} else {
			System.out.println("Insufficient funds or invalid amount.");
			return false;
		}
	}

}
