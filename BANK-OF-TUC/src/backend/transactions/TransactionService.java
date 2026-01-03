package backend.transactions;

import java.util.*;

import backend.accounts.Account;
import backend.transactions.validation.AmountValidationStrategy;
import backend.transactions.validation.BalanceValidationStrategy;
import backend.transactions.validation.IbanValidationStrategy;
import backend.transactions.validation.TransactionValidationStrategy;
import services.Command;
import types.TransactionType;
import backend.transactions.FileTransactionDAO;


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


    public synchronized void record(Transaction tx) {
        if (tx == null) return;

        // Safety fallback: αν για κάποιο λόγο δεν έχει γίνει injection από FileBankSystemDAO
        if (transactionDAO == null) {
            transactionDAO = new FileTransactionDAO();
        }

        transactionDAO.save(tx);
    }

	@Override
	public Transaction deposit(Account target, double amount) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Transaction withdraw(Account source, double amount) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Transaction transfer(Account from, Account to, double amount) {
		// TODO Auto-generated method stub
		return null;
	}

}
