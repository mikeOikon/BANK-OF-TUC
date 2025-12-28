package services.account_services;

import backend.BankSystem;
import backend.accounts.BusinessAccount;
import services.Command;

public class ApplyMonthlyFeeCommand implements Command {

    private final BusinessAccount account;
    private final double oldBalance;
    private final BankSystem bankSystem;

    public ApplyMonthlyFeeCommand(BusinessAccount account) {
        this.account = account;
        this.oldBalance = account.getBalance();
        this.bankSystem = BankSystem.getInstance();
    }

    @Override
    public void execute() {
        account.applyMonthlyFee();
        bankSystem.saveAllData();
    }

    @Override
    public void undo() {
        account.setBalance(oldBalance);
        bankSystem.saveAllData();
    }
}
