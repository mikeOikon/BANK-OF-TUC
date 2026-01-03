package backend.transactions;

import backend.accounts.Account;
import backend.support.Bill;
import types.TransactionType;


public class TransactionFactory {

    public static Transaction createTransaction(TransactionType type, TransactionBuilder builder) {
        if (type == null)
            throw new IllegalArgumentException("Transaction type must be specified.");

        Transaction tx;

        switch (type) {

            case DEPOSIT:
                if (builder.getTo() == null)
                    throw new IllegalArgumentException("Deposit needs a target account.");
                tx = new DepositTransaction(builder.getAmount(), builder.getTo());
                builder.getTo().getTransactions().push(tx);
                return tx;

            case WITHDRAW:
                if (builder.getFrom() == null)
                    throw new IllegalArgumentException("Withdraw needs a source account.");
                tx = new WithdrawTransaction(builder.getFrom(), builder.getAmount());
                builder.getFrom().getTransactions().push(tx);
                return tx;

            case TRANSFER:
                if (builder.getFrom() == null || builder.getTo() == null)
                    throw new IllegalArgumentException("Transfer needs both source and target accounts.");
                tx = new TransferTransaction(builder.getFrom(), builder.getTo(), builder.getAmount());
                builder.getFrom().getTransactions().push(tx);
                builder.getTo().getTransactions().push(tx);
                System.out.println(tx);
                return tx;

            case BILL_PAYMENT:
                if (builder.getFrom() == null || builder.getBill() == null)
                    throw new IllegalArgumentException("Bill payment needs a source account and a bill.");
                tx = new BillPaymentTransaction(builder.getFrom(), builder.getTo(), builder.getAmount(), builder.getBill());
                System.out.println("Creating BillPaymentTransaction from " + builder.getFrom().getIBAN() + " for bill " + builder.getBill().getPaymentCode());
                System.out.println(tx);
                builder.getFrom().getTransactions().push(tx);
                builder.getTo().getTransactions().push(tx);
                System.out.println("BillPaymentTransaction created and added to transactions.");
                return tx;

            case AUTO_BILL_PAYMENT:
                if (builder.getFrom() == null || builder.getBill() == null)
                    throw new IllegalArgumentException("Auto bill payment needs a source account and a bill.");
                tx = new AutoBillPaymentTransaction(builder.getFrom(), builder.getBill());
                builder.getFrom().getTransactions().push(tx);
                return tx;

            case INTEREST:
                if (builder.getTo() == null)
                    throw new IllegalArgumentException("Interest transaction needs a target account.");
                tx = new InterestTransaction(builder.getTo(), builder.getAmount());
                builder.getTo().getTransactions().push(tx);
                return tx;

            case FEE:
                if (builder.getTo() == null)
                    throw new IllegalArgumentException("Fee transaction needs a target account.");
                tx = new FeeTransaction(builder.getTo(), builder.getAmount(), builder.getDescription());
                builder.getTo().getTransactions().push(tx);
                return tx;

            default:
                throw new IllegalArgumentException("Unsupported transaction type: " + type);
        }
    }
}
