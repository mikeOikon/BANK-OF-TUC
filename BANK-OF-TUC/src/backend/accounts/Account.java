package backend.accounts;

import java.util.Random;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

import backend.Branch;
import backend.transactions.DepositTransaction;
import backend.transactions.Transaction;
import backend.transactions.TransferTransaction;
import backend.transactions.WithdrawTransaction;
import types.AccountType;

public abstract class Account {

	String IBAN;	//εχει συγκεκριμενο αλγοριθμο 
	String userID;
	double balance;
	Stack<Transaction> transactions; //ωστε να εμφανιζει πρωτη αυτη που έγινε τελευταία
	private static long nextAccountId = 1;
	private long account_id;
	private static Set<String> usedAccounts = new HashSet<>(); // για να κρατάμε τους ήδη χρησιμοποιημένους αριθμούς λογαριασμών 
	private double interest; //για επιτοκιο
	
	private Branch branch;	//για να συνδεσουμε με υποκαταστημα
	
	public Account(String IBAN, String userID, double balance, Stack<Transaction> transactions, double interest ,Branch branch) {
		if (IBAN != null && !IBAN.isEmpty()) 
			this.IBAN = IBAN;
		else
			this.IBAN = generateIBAN(branch); // Δημιουργία IBAN με branch
		this.interest = interest;
		this.userID = userID;
		this.balance = balance;
		this.transactions = transactions;
		this.account_id = nextAccountId++;
		this.branch = branch;	
	}

	public Branch getBranch() {
        return branch;
    }
	
	public String getIBAN() {
		return IBAN;
	}

	protected void setIBAN(String iBAN) {
		IBAN = iBAN;
	}


	protected String getUserID() {
		return userID;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}


	public Stack<Transaction> getTransactions() {
		return transactions;
	}

	protected double getInterest() {
		return interest;
	}

	protected void setInterest(double interest) {
		this.interest = interest;
	}

	protected long getAccount_id() {
		return account_id;
	}


	protected void setAccount_id(long account_id) {
		this.account_id = account_id;
	}
	
	public void deposit(double amount) {
	    if (amount <= 0)
	        throw new IllegalArgumentException("Deposit amount must be positive.");

	    this.balance += amount;

	    Transaction tx = new DepositTransaction(this, amount);
	    transactions.push(tx);
	}

	public void withdraw(double amount) {
	    if (amount <= 0)
	        throw new IllegalArgumentException("Withdraw amount must be positive.");

	    if (this.balance < amount)
	        throw new IllegalArgumentException("Insufficient funds.");

	    this.balance -= amount;

	    Transaction tx = new WithdrawTransaction(this, amount);
	    transactions.push(tx);
	}

	public void transferTo(Account target, double amount) {
	    if (target == null)
	        throw new IllegalArgumentException("Target account cannot be null.");

	    if (amount <= 0)
	        throw new IllegalArgumentException("Amount must be positive.");

	    if (this.balance < amount)
	        throw new IllegalArgumentException("Insufficient funds.");

	    // Update balances
	    this.balance -= amount;
	    target.balance += amount;

	    // Create transfer transaction
	    Transaction tx = new TransferTransaction(this, target, amount);

	    // Add to both accounts' histories
	    this.transactions.push(tx);
	    target.transactions.push(tx);
	}

	
// Μετατροπή χαρακτήρων σε ψηφία σύμφωνα με τον κανόνα IBAN (A=10 ... Z=35) ΓΙΑ ΝΑ ΜΠΟΡΕΙ ΝΑ ΑΛΛΑΖΕΙ ΑΝΑΛΟΓΩΣ ΤΗΝ ΤΡΑΠΕΖΑ
	 private static String lettersToDigits(String input) {
	        StringBuilder sb = new StringBuilder();
	        for (char ch : input.toCharArray()) {
	            if (Character.isLetter(ch)) {
	                int val = Character.toUpperCase(ch) - 'A' + 10;
	                sb.append(val);
	            } else {
	                sb.append(ch);
	            }
	        }
	        return sb.toString();
	    }
	    
	  public static String createUniqueBban(Branch branch) {
	        String accountNumber;
	        String bban;
	        
	        Random random = new Random();

	        do {
	            // Τυχαίος αριθμός λογαριασμού 16 ψηφίων
	            long accNum = Math.abs(random.nextLong()) % 1_0000_0000_0000_0000L;  // 16 ψηφία mask
	            accountNumber = String.format("%016d", accNum);  // συμπλήρωση με μηδενικά αριστερά

	            bban = branch.getBankCode() + branch.getBranchCode() + accountNumber;
	        } while (usedAccounts.contains(bban)); // έλεγχος μοναδικότητας

	        usedAccounts.add(bban); // κρατάμε το BBAN για να μην ξαναχρησιμοποιηθεί

	        return bban;
	    }
	    

	  // Υπολογισμός IBAN Ελλάδας από BBAN
	  public static String generateIBAN(Branch branch) {
		    String bban = createUniqueBban(branch);
	        final String country = "GR";

	        if (!bban.matches("\\d+")) {
	            throw new IllegalArgumentException("Το BBAN πρέπει να περιέχει μόνο ψηφία.");
	        }
	        if (bban.length() != 23) {
	            throw new IllegalArgumentException("Το BBAN για Ελλάδα πρέπει να έχει ακριβώς 23 ψηφία.");
	        }

	        // Αναδιάταξη: BBAN + GR + "00"
	        String rearranged = bban + country + "00";

	        // Μετατροπή γραμμάτων σε αριθμούς
	        String numericString = lettersToDigits(rearranged);

	        // Υπολογισμός mod 97 (σε μεγάλα νούμερα, με Long.parseLong κομμάτι-κομμάτι)
	        int mod = mod97(numericString);

	        int checkDigits = 98 - mod;
	        String checkStr = String.format("%02d", checkDigits);

	        System.out.println("Δημιουργήθηκε νέο IBAN: " + country + checkStr + bban);
	        
	        return country + checkStr + bban;
	    }

	    // Υπολογισμός mod 97 σε μεγάλους αριθμούς με τμηματική διαίρεση
	    private static int mod97(String numeric) {
	        int remainder = 0;
	        for (int i = 0; i < numeric.length(); i++) {
	            int digit = numeric.charAt(i) - '0';
	            remainder = (remainder * 10 + digit) % 97;
	        }
	        return remainder;
	    }
	    
	    @Override
	    public String toString() {
	        return String.format(
	            "%s | IBAN: %s | Balance: %.2f | Branch: %s",
	            this.getClass().getSimpleName(),  // επιστρέφει π.χ. "TransactionalAccount"
	            this.getIBAN(),
	            this.getBalance(),
	            (branch != null ? branch.getBranchCode() : "N/A")
	        );
	    }

		public AccountType getAccountType() {
			// TODO Auto-generated method stub
			return null;
		}
	    
	}

