package backend.accounts;

public class BankAccount {
	
	private String accountNumber; // Αριθμός λογαριασμού
	private double balance;
	private Branch branch;

	public BankAccount(String accountNumber, Branch branch) {
		this.accountNumber = accountNumber;
		this.balance = 100000000.0; // Αρχικό υπόλοιπο
		this.branch = branch;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public double getBalance() {
		return balance;
	}

	public Branch getBranch() {
		return branch;
	}
	
	public long payClient(long amount) { //επιστροφή ποσού που πληρώθηκε
		if (amount > 0 && amount <= balance) {
			balance -= amount;
			return amount;
		} else {
			System.out.println("Invalid amount or insufficient funds.");
			return 0;
		}
	}
	
	public void receivePayment(double amount) {
		if (amount > 0) {
			balance += amount;
		} else {
			System.out.println("Invalid amount.");
		}
	}

	//θα πρεπει η μεθοδος να καλειται καθε μηνα
	public void mothlyIncome() { //θεωρητικά λεφτά που μπαίνουν κάθε μήνα από χορηγίες και δανεια
		balance += 4500000.0;
	}

}
