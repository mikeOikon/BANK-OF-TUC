package backend.accounts;

import java.util.ArrayList;
import java.util.Random;
import java.util.HashSet;
import java.util.Set;


public abstract class Account {

	String IBAN;	//εχει συγκεκριμενο αλγοριθμο 
	String User; //name...
	double balance;
	ArrayList<String> transactions; //na doume an theloume na einai ArrayList
	long account_id; //used to link account to user(s)
	private static final Set<String> usedAccounts = new HashSet<>(); //***SOS εδω εχουμε μόνο το Bban +να δοUμε αν θελουμε να ειναι set
	//   ΜΑΙΝ  private static final String bankCode = "021"; // κωδικός τράπεζας Bank of TUC
	//private static final String branchCode = "0021"; // κωδικός καταστήματος (υποκαταστήματος) Main Branch
	
	public Account(String iBAN, String user, double balance, ArrayList<String> transactions) {
		super();
		this.IBAN = iBAN;
		this.User = user;
		this.balance = balance;
		this.transactions = transactions;
		this.account_id = account_id;
	}


	protected String getIBAN() {
		return IBAN;
	}


	protected void setIBAN(String iBAN) {
		//to IBAN exei GR + 2 check digits + 3 ψηφία κωδικό τράπεζας + 4 ψηφία κωδικός καταστήματος + 16 ψηφία αριθμός λογαριασμού
		IBAN = iBAN;
	}


	protected String getUser() {
		return User;
	}


	protected void setUser(String user) {
		User = user;
	}


	protected double getBalance() {
		return balance;
	}


	protected void setBalance(double balance) {
		this.balance = balance;
	}


	protected ArrayList<String> getTransactions() {
		return transactions;
	}


	protected void setTransactions(ArrayList<String> transactions) {
		this.transactions = transactions;
	}


	protected long getAccount_id() {
		return account_id;
	}


	protected void setAccount_id(long account_id) {
		this.account_id = account_id;
	}
	
	//protected void createAccount()
	
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
	    
	  public static String createUniqueBban(String bankCode, String branchCode) {
	        String accountNumber;
	        String bban;
	        
	        Random random = new Random();

	        do {
	            // Τυχαίος αριθμός λογαριασμού 16 ψηφίων
	            long accNum = Math.abs(random.nextLong()) % 1_0000_0000_0000_0000L;
	            accountNumber = String.format("%016d", accNum);

	            bban = bankCode + branchCode + accountNumber;
	        } while (usedAccounts.contains(bban)); // έλεγχος μοναδικότητας

	        //sos den jerv an prepei na kratame to bban h mono to iban
	        usedAccounts.add(bban); // κρατάμε το BBAN για να μην ξαναχρησιμοποιηθεί

	        System.out.println("Δημιουργήθηκε νέο BBAN: " + bban);
	        
	        return bban;
	    }
	    

	  // Υπολογισμός IBAN Ελλάδας από BBAN
	  public static String generateIBAN(String bankCode, String branchCode) {
		    String bban = createUniqueBban(bankCode, branchCode);
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

