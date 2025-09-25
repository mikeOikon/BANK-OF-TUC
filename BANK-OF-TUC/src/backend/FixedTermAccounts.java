package backend;

public class FixedTermAccounts extends Account {
	
	//auto generated
	private String accountNumber;
	private double balance;
	private double interestRate;
	private String maturityDate;
	
	public FixedTermAccounts(String accountNumber, double initialDeposit, double interestRate, String maturityDate) {
		this.accountNumber = accountNumber;
		this.balance = initialDeposit;
		this.interestRate = interestRate;
		this.maturityDate = maturityDate;
	}
	
	public String getAccountNumber() {
		return accountNumber;
	}
	
	public double getBalance() {
		return balance;
	}
	
	public double getInterestRate() {
		return interestRate;
	}
	
	public String getMaturityDate() {
		return maturityDate;
	}
	
	public void deposit(double amount) {
		if (amount > 0) {
			balance += amount;
		} else {
			throw new IllegalArgumentException("Deposit amount must be positive.");
		}
	}
	
	public void withdraw(double amount) {
		if (amount > 0 && amount <= balance) {
			balance -= amount;
		} else {
			throw new IllegalArgumentException("Invalid withdrawal amount.");
		}
	}
	
	public void applyInterest() {
		balance += balance * interestRate / 100;
	}

}
