package backend;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import backend.accounts.*;
import backend.transactions.*;

public class GsonConfig {

    public static Gson build() {

        // Account polymorphism
        RuntimeTypeAdapterFactory<Account> accountAdapter =
                RuntimeTypeAdapterFactory
                        .of(Account.class, "accountType")   // <--- KEY added to JSON
                        .registerSubtype(TransactionalAccount.class, "transactional")
                        .registerSubtype(SavingsAccount.class, "savings")
                        .registerSubtype(FixedTermAccount.class, "fixed")
                        .registerSubtype(BusinessAccount.class, "business");

        // Transaction polymorphism
        RuntimeTypeAdapterFactory<Transaction> transactionAdapter =
                RuntimeTypeAdapterFactory
                        .of(Transaction.class, "txType")
                        .registerSubtype(DepositTransaction.class, "deposit")
                        .registerSubtype(WithdrawTransaction.class, "withdraw")
                        .registerSubtype(TransferTransaction.class, "transfer");

        return new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapterFactory(accountAdapter)
                .registerTypeAdapterFactory(transactionAdapter)
                .create();
    }
}
