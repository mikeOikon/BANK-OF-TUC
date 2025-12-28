package backend.transactions;

import backend.FileLogger;
import backend.GsonConfig;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO που αποθηκεύει συναλλαγές σε JSON αρχείο (audit log).
 * Είναι ανεξάρτητο από το bankSystem.json ώστε να φαίνεται καθαρά το DAO pattern.
 */
public class FileTransactionDAO implements TransactionDAO {

    private static final String DATA_FILE = "data/transactions.json";
    private static final Type LIST_TYPE = new TypeToken<List<Transaction>>() {}.getType();

    private final Gson gson = GsonConfig.build();

    private File ensureFileExists() {
        File dir = new File("data");
        if (!dir.exists()) dir.mkdir();

        File file = new File(DATA_FILE);
        if (!file.exists()) {
            try (FileWriter writer = new FileWriter(file)) {
                writer.write("[]");
            } catch (Exception e) {
                FileLogger.getInstance().log(types.LogLevel.ERROR, types.LogCategory.SYSTEM,
                        "[DAO] Failed to initialize transactions file: " + e.getMessage());
            }
        }
        return file;
    }

    private List<Transaction> readAllInternal() {
        File file = ensureFileExists();
        try (FileReader reader = new FileReader(file)) {
            List<Transaction> list = gson.fromJson(reader, LIST_TYPE);
            return (list == null) ? new ArrayList<>() : list;
        } catch (Exception e) {
            FileLogger.getInstance().log(types.LogLevel.ERROR, types.LogCategory.SYSTEM,
                    "[DAO] Failed to read transactions: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    private void writeAllInternal(List<Transaction> transactions) {
        File file = ensureFileExists();
        try (FileWriter writer = new FileWriter(file)) {
            gson.toJson(transactions, writer);
        } catch (Exception e) {
            FileLogger.getInstance().log(types.LogLevel.ERROR, types.LogCategory.SYSTEM,
                    "[DAO] Failed to write transactions: " + e.getMessage());
        }
    }

    @Override
    public synchronized void save(Transaction transaction) {
        if (transaction == null) return;
        List<Transaction> all = readAllInternal();
        all.add(transaction);
        writeAllInternal(all);
    }

    @Override
    public synchronized List<Transaction> findByAccountIban(String iban) {
        List<Transaction> all = readAllInternal();
        List<Transaction> result = new ArrayList<>();
        if (iban == null || iban.isBlank()) return result;

        for (Transaction tx : all) {
            if (tx == null) continue;
            if (iban.equals(tx.getFromAccountIban()) || iban.equals(tx.getToAccountIban())) {
                result.add(tx);
            }
        }
        return result;
    }

    @Override
    public synchronized List<Transaction> findAll() {
        return readAllInternal();
    }
}
