package backend.transactions;

import java.util.*;

import backend.accounts.Account;
import backend.transactions.validation.AmountValidationStrategy;
import backend.transactions.validation.BalanceValidationStrategy;
import backend.transactions.validation.IbanValidationStrategy;
import backend.transactions.validation.TransactionValidationStrategy;
import services.Command;
import types.TransactionType;

/**
 * TransactionService
 *
 *  - Singleton: ένα και μοναδικό instance σε όλο το σύστημα
 *  - Χρησιμοποιεί TransactionFactory για να δημιουργεί Transaction objects (Factory pattern)
 *  - Χρησιμοποιεί Command pattern (DepositCommand, WithdrawCommand, TransferCommand)
 *  - Κρατά ιστορικό εντολών για πιθανό undo / auditing
 */
public class TransactionService implements TransactionAPI {
    // ----------- SINGLETON -----------
    private static TransactionService instance;
    private final Map<TransactionType, List<TransactionValidationStrategy>> validationMap = new HashMap<>();

    private TransactionDAO transactionDAO;

    private TransactionService() {
        validationMap.put(TransactionType.DEPOSIT, Arrays.asList(
                new AmountValidationStrategy(),
                new IbanValidationStrategy()
        ));
        validationMap.put(TransactionType.WITHDRAW, Arrays.asList(
                new AmountValidationStrategy(),
                new BalanceValidationStrategy(),
                new IbanValidationStrategy()
        ));

        validationMap.put(TransactionType.TRANSFER, Arrays.asList(
                new AmountValidationStrategy(),
                new BalanceValidationStrategy(),
                new IbanValidationStrategy()
        ));
    }

    public static synchronized TransactionService getInstance() {
        if (instance == null) {
            instance = new TransactionService();
        }
        return instance;
    }

    public void setTransactionDAO(TransactionDAO transactionDAO) {
        this.transactionDAO = transactionDAO;
    }
    // ======================================
    //        PUBLIC API (GUI / CLI)
    // ======================================

    public Transaction deposit(Account target, double amount) {
        DepositCommand cmd = new DepositCommand(target, amount);
        cmd.execute();
        return cmd.getTransaction();
    }

    public Transaction withdraw(Account source, double amount) {
        WithdrawCommand cmd = new WithdrawCommand(source, amount);
        cmd.execute();
        return cmd.getTransaction();
    }
    public Transaction transfer(Account from, Account to, double amount) {
        TransferCommand cmd = new TransferCommand(from, to, amount);
        cmd.execute();
        return cmd.getTransaction();
    }

    // ======================================
    //           BASE COMMAND CLASS
    // ======================================
    private abstract class BaseTransactionCommand implements Command {
        protected final double amount;
        protected Transaction transaction;

        protected BaseTransactionCommand(double amount) {
            this.amount = amount;
        }

        public Transaction getTransaction() {
            return transaction;
        }

        protected void persist(Transaction tx) {
            if (transactionDAO != null) {
                transactionDAO.save(tx);
            }
        }

    }
    private void applyValidation(TransactionType type, Account from, Account to, double amount) {
        List<TransactionValidationStrategy> strategies = validationMap.get(type);
        if (strategies == null) {
            return; // ή πέτα exception αν θες να είναι υποχρεωτικό
        }
        for (TransactionValidationStrategy strategy : strategies) {
            strategy.validate(from, to, amount);
        }
    }


    // ======================================
    //             COMMANDS
    // ======================================
    private class DepositCommand extends BaseTransactionCommand {

        private final Account target;

        public DepositCommand(Account target, double amount) {
            super(amount);
            this.target = target;
        }

        @Override
        public void execute() {
            // Strategy validation
            applyValidation(TransactionType.DEPOSIT, null, target, amount);

            // Execute deposit
            target.setBalance(target.getBalance() + amount);

            // Create transaction through factory
            transaction = TransactionFactory.createTransaction(
                    TransactionType.DEPOSIT,
                    target,
                    amount
            );

            // Push transaction to account history
            target.getTransactions().push(transaction);

            // Save using DAO
            persist(transaction);
        }

		@Override
		public void undo() {
			// TODO Auto-generated method stub
			
		}
    }

    private class WithdrawCommand extends BaseTransactionCommand {

        private final Account source;

        public WithdrawCommand(Account source, double amount) {
            super(amount);
            this.source = source;
        }

        @Override
        public void execute() {
            applyValidation(TransactionType.WITHDRAW, source, null, amount);

            source.setBalance(source.getBalance() - amount);

            transaction = TransactionFactory.createTransaction(
                    TransactionType.WITHDRAW,
                    source,
                    amount
            );

            source.getTransactions().push(transaction);
            persist(transaction);
        }

		@Override
		public void undo() {
			// TODO Auto-generated method stub
			
		}
    }

    private class TransferCommand extends BaseTransactionCommand {

        private final Account from;
        private final Account to;

        public TransferCommand(Account from, Account to, double amount) {
            super(amount);
            this.from = from;
            this.to = to;
        }

        @Override
        public void execute() {
            applyValidation(TransactionType.TRANSFER, from, to, amount);

            from.setBalance(from.getBalance() - amount);
            to.setBalance(to.getBalance() + amount);

            transaction = TransactionFactory.createTransaction(from, to, amount);

            from.getTransactions().push(transaction);
            to.getTransactions().push(transaction);

            persist(transaction);
        }

		@Override
		public void undo() {
			// TODO Auto-generated method stub
			
		}
    }

}
