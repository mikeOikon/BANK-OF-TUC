package backend.transactions;

import backend.accounts.Account;
import backend.support.Bill;
import backend.BankSystem;
import types.TransactionType;

public class AutoBillPaymentTransaction extends Transaction {

    private transient Account fromAccount;
    private transient Bill bill;

    public AutoBillPaymentTransaction(Account fromAccount, Bill bill) {
        super(
                TransactionType.AUTO_BILL_PAYMENT,
                bill.getAmount(),
                fromAccount.getIBAN(),
                bill.getBusinessIBAN(),
                "Auto Bill Payment: " + bill.getPaymentCode()
        );
        this.fromAccount = fromAccount;
        this.bill = bill;
        execute();
    }

    private void execute() {
        BankSystem bankSystem = BankSystem.getInstance();

        if (fromAccount.isFrozen()) {
            throw new IllegalStateException("Account is frozen");
        }

        if (fromAccount.getBalance() < bill.getAmount()) {
            throw new IllegalStateException("Insufficient funds for auto bill payment");
        }

        // Χρέωση πελάτη
        fromAccount.setBalance(fromAccount.getBalance() - bill.getAmount());

        // Πίστωση επιχείρησης
        Account businessAcc = bankSystem.getAccountbyNumber(bill.getBusinessIBAN());
        if (businessAcc != null) {
            businessAcc.setBalance(businessAcc.getBalance() + bill.getAmount());
            businessAcc.getTransactions().push(this);
        }

        // Καταγραφή συναλλαγής στον πελάτη
        fromAccount.getTransactions().push(this);

        // Σήμανση bill
        bill.setPaid(true);

        // Αποθήκευση
        bankSystem.dao.save(bankSystem);
    }
}
