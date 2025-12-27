package backend;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.google.gson.Gson;

public class FileBankSystemDAO implements BankSystemDAO {

    private static final String DATA_FILE = "data/bankSystem.json";
    private final Gson gson = GsonConfig.build();

    @Override
    public BankSystem load() {
        File file = new File(DATA_FILE);

        if (!file.exists()) {
            //System.out.println("[DAO] No saved data found. Creating new system.");
        	BankSystem system= BankSystem.createEmptySystem();
        	system.createDefaultAdminIfMissing();       	
            return system; 
        }

        try (FileReader reader = new FileReader(file)) {
            BankSystem system = gson.fromJson(reader, BankSystem.class);

            if (system == null) {
                return BankSystem.createEmptySystem();
            }

            system.rebuildTransientState(); 
            //System.out.println("[DAO] Data loaded successfully.");
            return system;

        } catch (Exception e) {
            //System.out.println("[DAO] Failed to load data: " + e.getMessage());
            return BankSystem.createEmptySystem();
        }
    }

    @Override
    public void save(BankSystem system) {
        try {
            File dir = new File("data");
            if (!dir.exists()) dir.mkdir();

            try (FileWriter writer = new FileWriter(DATA_FILE)) {
                gson.toJson(system, writer);
            }

            System.out.println("[DAO] Data saved successfully.");

        } catch (IOException e) {
            System.err.println("[DAO] Failed to save data: " + e.getMessage());
        }
    }
}

