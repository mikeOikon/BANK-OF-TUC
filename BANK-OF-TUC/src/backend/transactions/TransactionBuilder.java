package backend.transactions;

import backend.accounts.Account;
import backend.support.Bill;
import types.TransactionType;

public class TransactionBuilder {

    private TransactionType type;
    private Account from;
    private Account to;
    private double amount;
    private Bill bill; // για Bill Payments

    // Fluent setters
    public TransactionBuilder setType(TransactionType type) { this.type = type; return this; }
    public TransactionBuilder setFrom(Account from) { this.from = from; return this; }
    public TransactionBuilder setTo(Account to) { this.to = to; return this; }
    public TransactionBuilder setAmount(double amount) { this.amount = amount; return this; }
    public TransactionBuilder setBill(Bill bill) { this.bill = bill; return this; }

    public Transaction build() {
        if (type == null) throw new IllegalStateException("Transaction type is missing.");
        if (amount <= 0 && type != TransactionType.AUTO_BILL_PAYMENT) // AUTO_BILL_PAYMENT μπορεί να χρησιμοποιεί το bill.getAmount()
            throw new IllegalStateException("Amount must be > 0.");

        return switch (type) {
            case DEPOSIT -> {
                if (to == null) throw new IllegalStateException("Deposit needs a target account.");
                yield TransactionFactory.createTransaction(TransactionType.DEPOSIT, to, amount);
            }
            case WITHDRAW -> {
                if (from == null) throw new IllegalStateException("Withdraw needs a source account.");
                yield TransactionFactory.createTransaction(TransactionType.WITHDRAW, from, amount);
            }
            case TRANSFER -> {
                if (from == null || to == null) throw new IllegalStateException("Transfer needs both source and target accounts.");
                yield TransactionFactory.createTransaction(from, to, amount);
            }
            case BILL_PAYMENT -> {
                if (from == null || bill == null) throw new IllegalStateException("BillPayment needs source account and bill.");
                yield TransactionFactory.createBillPaymentTransaction(from, bill);
            }
            case AUTO_BILL_PAYMENT -> {
                if (from == null || bill == null) throw new IllegalStateException("AutoBillPayment needs source account and bill.");
                yield TransactionFactory.createAutoBillPaymentTransaction(from, bill);
            }
            case INTEREST -> {
                if (to == null) throw new IllegalStateException("InterestTransaction needs a target account.");
                yield TransactionFactory.createInterestTransaction(to, amount);
            }
            case FEE -> {
                if (from == null) throw new IllegalStateException("FeeTransaction needs a target account.");
                yield TransactionFactory.createFeeTransaction(from, amount, "Fee applied");
            }
            default -> throw new UnsupportedOperationException("Unsupported transaction type.");
        };
    }
}
