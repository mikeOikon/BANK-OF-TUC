package backend;

import com.google.gson.*;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import backend.accounts.*;
import backend.transactions.*;

public class GsonConfig {

    public static Gson build() {

        // Account polymorphism
        RuntimeTypeAdapterFactory<Account> accountAdapter =
                RuntimeTypeAdapterFactory
                        .of(Account.class, "accountType")
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
                // --- Η ΠΡΟΣΘΗΚΗ ΠΟΥ ΛΕΙΠΕΙ ---
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .registerTypeAdapter(LocalDateTime.class,new LocalDateTimeAdapter())
                // ----------------------------
                .registerTypeAdapterFactory(accountAdapter)
                .registerTypeAdapterFactory(transactionAdapter)
                .create();
    }

    /**
     * Εσωτερική βοηθητική κλάση για τη διαχείριση του LocalDate
     */
    private static class LocalDateAdapter implements JsonSerializer<LocalDate>, JsonDeserializer<LocalDate> {
        private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;

        @Override
        public JsonElement serialize(LocalDate date, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(date.format(formatter));
        }

        @Override
        public LocalDate deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            return LocalDate.parse(json.getAsString(), formatter);
        }
    }
}