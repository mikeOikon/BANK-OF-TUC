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

        // Transaction polymorphism - ΕΝΗΜΕΡΩΜΕΝΟ
        RuntimeTypeAdapterFactory<Transaction> transactionAdapter =
                RuntimeTypeAdapterFactory
                        .of(Transaction.class, "txType")
                        .registerSubtype(DepositTransaction.class, "deposit")
                        .registerSubtype(WithdrawTransaction.class, "withdraw")
                        .registerSubtype(TransferTransaction.class, "transfer")
                        // ΠΡΟΣΘΗΚΕΣ ΓΙΑ ΤΗΝ ΕΠΙΛΥΣΗ ΤΟΥ ERROR:
                        .registerSubtype(BillPaymentTransaction.class, "bill_payment")
                        .registerSubtype(AutoBillPaymentTransaction.class, "auto_bill_payment")
                        // Προληπτικές προσθήκες για τα υπόλοιπα types:
                        .registerSubtype(InterestTransaction.class, "interest")
                        .registerSubtype(FeeTransaction.class, "fee");

        return new GsonBuilder()
                .setPrettyPrinting()
                // Καταγραφή των Adapters για ημερομηνίες
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                // Καταγραφή των Factory Adapters για τον πολυμορφισμό
                .registerTypeAdapterFactory(accountAdapter)
                .registerTypeAdapterFactory(transactionAdapter)
                .create();
    }

    /**
     * Adapter για LocalDate
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

    /**
     * Adapter για LocalDateTime (Χρειάζεται επίσης για το timestamp των Transactions)
     */
    private static class LocalDateTimeAdapter implements JsonSerializer<LocalDateTime>, JsonDeserializer<LocalDateTime> {
        private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

        @Override
        public JsonElement serialize(LocalDateTime dateTime, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(dateTime.format(formatter));
        }

        @Override
        public LocalDateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            return LocalDateTime.parse(json.getAsString(), formatter);
        }
    }
}