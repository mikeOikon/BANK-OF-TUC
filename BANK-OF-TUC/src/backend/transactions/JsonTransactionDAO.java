package backend.transactions;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class JsonTransactionDAO implements TransactionDAO {

    private final String filePath;
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private final List<Transaction> transactions = new ArrayList<>();

    public JsonTransactionDAO(String filePath) {
        this.filePath = filePath;
        load();
    }

    @Override
    public void save(Transaction transaction) {
        transactions.add(transaction);
        saveAll();
    }

    @Override
    public List<Transaction> findByAccountIban(String iban) {
        List<Transaction> result = new ArrayList<>();
        for (Transaction tx : transactions) {
            if (iban.equals(tx.getFromAccountIban()) || iban.equals(tx.getToAccountIban())) {
                result.add(tx);
            }
        }
        return result;
    }

    @Override
    public List<Transaction> findAll() {
        return new ArrayList<>(transactions);
    }

    private void load() {
        try (FileReader reader = new FileReader(filePath)) {
            Transaction[] arr = gson.fromJson(reader, Transaction[].class);
            if (arr != null) {
                transactions.addAll(Arrays.asList(arr));
            }
        } catch (IOException e) {
            System.out.println("No transactions file yet, starting fresh.");
        }
    }

    private void saveAll() {
        try (FileWriter writer = new FileWriter(filePath)) {
            gson.toJson(transactions, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
