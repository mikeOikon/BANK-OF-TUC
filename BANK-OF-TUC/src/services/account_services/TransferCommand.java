package services.account_services;

import backend.BankSystem;
import backend.accounts.Account;
import services.Command;

public class TransferCommand implements Command {

    private final Account from;
    private final Account to;
    private final double amount;
    private final BankSystem bankSystem;

    public TransferCommand(Account from, Account to, double amount) {
        this.from = from;
        this.to = to;
        this.amount = amount;
        this.bankSystem = BankSystem.getInstance();
    }

    @Override
    public void execute() {
        from.transferTo(to, amount);
        bankSystem.saveAllData();
    }

    @Override
    public void undo() {
        to.transferTo(from, amount);
        bankSystem.saveAllData();
    }
}
