package services.account_services;

import backend.BankSystem;
import backend.accounts.Account;
import services.Command;

public class DepositCommand implements Command {

    private final Account account;
    private final double amount;
    private final BankSystem bankSystem;

    public DepositCommand(Account account, double amount) {
        this.account = account;
        this.amount = amount;
        this.bankSystem = BankSystem.getInstance();
    }

    @Override
    public void execute() {
        account.deposit(amount);
        bankSystem.saveAllData();
    }

    @Override
    public void undo() {
        account.withdraw(amount);
        bankSystem.saveAllData();
    }
}
