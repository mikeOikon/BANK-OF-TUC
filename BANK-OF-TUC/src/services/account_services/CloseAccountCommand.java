package services.account_services;

import backend.BankSystem;
import backend.accounts.Account;
import backend.users.Customer;
import backend.users.User;
import services.Command;

public class CloseAccountCommand implements Command {

    private final User executor;
    private final User targetUser;
    private final String iban;
    private Account removedAccount;

    public CloseAccountCommand(User executor, User targetUser, String iban) {
        this.executor = executor;
        this.targetUser = targetUser;
        this.iban = iban;
    }

    @Override
    public void execute() {
        if (!executor.canViewAllAccounts()) {
            throw new SecurityException("No permission to close accounts");
        }

        

        if (removedAccount == null) {
            throw new IllegalStateException("Account not found");
        }

        BankSystem.getInstance().dao.save(BankSystem.getInstance());
    }

    @Override
    public void undo() {
        if (removedAccount != null) {
            targetUser.getAccounts().add(removedAccount);
            BankSystem.getInstance().dao.save(BankSystem.getInstance());
        }
    }
}
