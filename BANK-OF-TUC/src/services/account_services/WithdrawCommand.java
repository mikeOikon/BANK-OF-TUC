package services.account_services;

import backend.BankSystem;
import backend.accounts.Account;
import services.Command;

public class WithdrawCommand implements Command {

    private final Account account;
    private final double amount;
    private final BankSystem bankSystem;

    public WithdrawCommand(Account account, double amount) {
        this.account = account;
        this.amount = amount;
        this.bankSystem = BankSystem.getInstance();
    }

    @Override
    public void execute() {
        account.withdraw(amount);
        bankSystem.saveAllData();
    }

    @Override
    public void undo() {
        account.deposit(amount);
        bankSystem.saveAllData();
    }
}
