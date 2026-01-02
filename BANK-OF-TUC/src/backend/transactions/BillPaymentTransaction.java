package backend.transactions;

import backend.accounts.Account;
import backend.support.Bill;
import backend.BankSystem;
import types.TransactionType;

public class BillPaymentTransaction extends Transaction {

    public BillPaymentTransaction(Account fromAccount, Bill bill) {
        super(
                TransactionType.BILL_PAYMENT,
                bill.getAmount(),
                fromAccount.getIBAN(),
                bill.getBusinessIBAN(),
                "Bill Payment: " + bill.getPaymentCode()
        );
        execute(fromAccount, bill);
    }

    private void execute(Account from, Bill bill) {
        // Πάρε το BankSystem instance
        BankSystem bankSystem = BankSystem.getInstance();

        // Χρέωση πελάτη
        from.setBalance(from.getBalance() - bill.getAmount());

        // Πίστωση επιχείρησης
        Account businessAcc = bankSystem.getAccountbyNumber(bill.getBusinessIBAN());
        if (businessAcc != null) {
            businessAcc.setBalance(businessAcc.getBalance() + bill.getAmount());
        }

        // Σήμανση bill ως πληρωμένου
        bill.setPaid(true);

        // Αποθήκευση
        bankSystem.dao.save(bankSystem);
    }
}
