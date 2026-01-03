package services.account_services;

import backend.BankSystem;
import backend.accounts.Account;
import services.Command;
import types.TransactionType;

public class TransactionCommand implements Command {

	private final TransactionType type;
	private final Account from;
	private final Account to;
	private final double amount;
	private final BankSystem bankSystem;

	public TransactionCommand(TransactionType type, Account from, Account to, double amount) {
		this.type = type;
		this.from = from;
		this.to = to;
		this.amount = amount;
		this.bankSystem = BankSystem.getInstance();
	}
	
	@Override
    public void execute() {
		bankSystem.transaction(type, from, to, amount);
        bankSystem.saveAllData();
    }

    @Override
    public void undo() {
        bankSystem.transaction(type, to, from, amount);
        bankSystem.saveAllData();
    }
}
