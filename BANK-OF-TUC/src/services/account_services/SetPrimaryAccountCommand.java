package services.account_services;

import backend.accounts.Account;
import backend.users.Customer;
import backend.users.BusinessCustomer;
import services.Command;

public class SetPrimaryAccountCommand implements Command {

    private final Account newPrimary;
    private final Object owner; // Customer ή BusinessCustomer
    private Account oldPrimary;

    public SetPrimaryAccountCommand(Account newPrimary, Object owner) {
        this.newPrimary = newPrimary;
        this.owner = owner;
        if (owner instanceof Customer c) {
            oldPrimary = c.getPrimaryAccount();
        } else if (owner instanceof BusinessCustomer b) {
            oldPrimary = b.getPrimaryAccount();
        }
    }

    @Override
    public void execute() {
        if (owner instanceof Customer c) {
            c.setPrimaryAccount(newPrimary);
        } else if (owner instanceof BusinessCustomer b) {
            b.setPrimaryAccount(newPrimary);
        }
    }

    @Override
    public void undo() {
        if (owner instanceof Customer c) {
            c.setPrimaryAccount(oldPrimary);
        } else if (owner instanceof BusinessCustomer b) {
            b.setPrimaryAccount(oldPrimary);
        }
    }
}