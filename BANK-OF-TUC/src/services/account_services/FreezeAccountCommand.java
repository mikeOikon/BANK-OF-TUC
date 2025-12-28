package services.account_services;

import backend.BankSystem;
import backend.accounts.Account;
import services.Command;

public class FreezeAccountCommand implements Command {

    private final Account account;
    private final BankSystem bankSystem;

    public FreezeAccountCommand(Account account) {
        this.account = account;
        this.bankSystem = BankSystem.getInstance();
    }

    @Override
    public void execute() {
        account.setFrozen(true);
        bankSystem.saveAllData();
    }

    @Override
    public void undo() {
        account.setFrozen(false);
        bankSystem.saveAllData();
    }
}
