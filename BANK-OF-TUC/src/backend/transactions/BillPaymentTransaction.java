package backend.transactions;

import backend.accounts.Account;
import backend.support.Bill;
import types.TransactionType;

/**
 * Απλό record συναλλαγής πληρωμής λογαριασμού.
 * ❗ Δεν αλλάζει balances
 * ❗ Δεν κάνει save
 * ❗ Δεν κρατά references σε Account ή Bill
 * ❗ Είναι 100% ασφαλές για Gson serialization
 */
public class BillPaymentTransaction extends Transaction {
	
	

    public BillPaymentTransaction(Account fromAccount, Account toAccount, double amount, Bill bill) {
        super(
            TransactionType.BILL_PAYMENT,
            amount,
            fromAccount.getIBAN(),
            toAccount.getIBAN(),
            "Bill Payment: " + bill.getPaymentCode()
        );
    }
    
}
