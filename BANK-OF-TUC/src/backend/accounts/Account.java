package backend.accounts;

import java.util.Random;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

public abstract class Account {

	String IBAN;	//εχει συγκεκριμενο αλγοριθμο 
	String userID;
	double balance;
	Stack<Transaction> transactions; //ωστε να εμφανιζει πρωτη αυτη που έγινε τελευταία
	private static long nextAccountId = 1;
	private long account_id;
	private static Set<String> usedAccounts = new HashSet<>(); // για να κρατάμε τους ήδη χρησιμοποιημένους αριθμούς λογαριασμών 
	
	private Branch branch;	//για να συνδεσουμε με υποκαταστημα
	
	public Account(String userID, double balance, Stack<Transaction> transactions, Branch branch) {
		super();
		this.IBAN = generateIBAN(branch); // Δημιουργία IBAN με branch
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

	protected long getAccount_id() {
		return account_id;
	}


	protected void setAccount_id(long account_id) {
		this.account_id = account_id;
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
	            long accNum = Math.abs(random.nextLong()) % 1_0000_0000_0000_0000L;
	            accountNumber = String.format("%016d", accNum);

	            bban = branch.getBankCode() + branch.getBranchCode() + accountNumber;
	        } while (usedAccounts.contains(bban)); // έλεγχος μοναδικότητας

	        //sos den jerv an prepei na kratame to bban h mono to iban
	        usedAccounts.add(bban); // κρατάμε το BBAN για να μην ξαναχρησιμοποιηθεί

	        System.out.println("Δημιουργήθηκε νέο BBAN: " + bban);
	        
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
	    
	    

	}

