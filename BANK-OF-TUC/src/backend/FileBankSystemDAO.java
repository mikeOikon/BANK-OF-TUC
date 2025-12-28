package backend;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import backend.transactions.FileTransactionDAO;
import backend.transactions.TransactionService;


import com.google.gson.Gson;

public class FileBankSystemDAO implements BankSystemDAO {

    private static final String DATA_FILE = "data/bankSystem.json";
    private final Gson gson = GsonConfig.build();

    private void initTransactionDAO() {
        TransactionService.getInstance().setTransactionDAO(new FileTransactionDAO());
    }

    private BankSystem newEmptySystem() {
        BankSystem system = BankSystem.createEmptySystem();
        system.createDefaultAdminIfMissing();
        system.rebuildTransientState();
        initTransactionDAO();
        return system;
    }

    @Override
    public BankSystem load() {
        File file = new File(DATA_FILE);
        FileLogger logger= FileLogger.getInstance();

        if (!file.exists()) {
            logger.log(types.LogLevel.WARNING, types.LogCategory.SYSTEM, "[DAO] Data file not found. Creating a new empty BankSystem.");
            return newEmptySystem();
        }

        try (FileReader reader = new FileReader(file)) {
            BankSystem system = gson.fromJson(reader, BankSystem.class);

            if (system == null) {
                logger.log(types.LogLevel.WARNING, types.LogCategory.SYSTEM, "[DAO] Data file was empty/unreadable. Creating a new empty BankSystem.");
                return newEmptySystem();
            }

            system.rebuildTransientState();
            initTransactionDAO();

            logger.log(types.LogLevel.INFO, types.LogCategory.SYSTEM, "[DAO] Data loaded successfully.");
            return system;

        } catch (Exception e) {
            logger.log(types.LogLevel.ERROR, types.LogCategory.SYSTEM, "[DAO] Failed to load data: " + e.getMessage());
            return newEmptySystem();
        }
    }

    @Override
    public void save(BankSystem system) {
        FileLogger logger = FileLogger.getInstance();
        try {
            File dir = new File("data");
            if (!dir.exists()) dir.mkdir();

            try (FileWriter writer = new FileWriter(DATA_FILE)) {
                gson.toJson(system, writer);
            }

            logger.log(types.LogLevel.INFO, types.LogCategory.SYSTEM, "[DAO] Data saved successfully.");

        } catch (IOException e) {
            logger.log(types.LogLevel.ERROR, types.LogCategory.SYSTEM, "[DAO] Failed to save data: " + e.getMessage());
        }
    }

}

