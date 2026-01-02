package backend.transactions;

import backend.accounts.Account;
import backend.support.Bill;
import backend.BankSystem;
import types.TransactionType;

public class AutoBillPaymentTransaction extends Transaction {

    public AutoBillPaymentTransaction(Account fromAccount, Bill bill) {
        super(
                TransactionType.AUTO_BILL_PAYMENT,
                bill.getAmount(),
                fromAccount.getIBAN(),
                bill.getBusinessIBAN(),
                "Auto Bill Payment: " + bill.getPaymentCode()
        );
        execute(fromAccount, bill);
    }

    private void execute(Account from, Bill bill) {
        BankSystem bankSystem = BankSystem.getInstance();

        if (from.getBalance() < bill.getAmount()) {
            throw new IllegalStateException("Insufficient funds for auto bill payment");
        }

        from.setBalance(from.getBalance() - bill.getAmount());

        Account businessAcc = bankSystem.getAccountbyNumber(bill.getBusinessIBAN());
        if (businessAcc != null) {
            businessAcc.setBalance(businessAcc.getBalance() + bill.getAmount());
        }

        bill.setPaid(true);

        bankSystem.dao.save(bankSystem);
    }
}
