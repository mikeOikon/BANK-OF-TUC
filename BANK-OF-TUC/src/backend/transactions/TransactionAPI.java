package backend.transactions;

import backend.accounts.Account;

public interface TransactionAPI {

    Transaction deposit(Account target, double amount);

    Transaction withdraw(Account source, double amount);

    Transaction transfer(Account from, Account to, double amount);
}
