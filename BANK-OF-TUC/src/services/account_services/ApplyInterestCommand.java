package services.account_services;

import backend.BankSystem;
import backend.accounts.Account;
import services.Command;
import types.TransactionType;

public class ApplyInterestCommand implements Command {

    private final Account account;
    private final double oldBalance;
    private final BankSystem bankSystem;

    public ApplyInterestCommand(Account account) {
        this.account = account;
        this.oldBalance = account.getBalance();
        this.bankSystem = BankSystem.getInstance();
    }

    @Override
    public void execute() {
        double interest = account.getBalance() * account.getInterest();
        bankSystem.transaction(TransactionType.DEPOSIT, account, null, interest);
        bankSystem.saveAllData();
    }

    @Override
    public void undo() {
        account.setBalance(oldBalance);
        bankSystem.saveAllData();
    }
}
