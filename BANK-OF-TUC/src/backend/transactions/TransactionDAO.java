package backend.transactions;

import java.util.List;

public interface TransactionDAO {

    void save(Transaction transaction);

    List<Transaction> findByAccountIban(String iban);

    List<Transaction> findAll();
}