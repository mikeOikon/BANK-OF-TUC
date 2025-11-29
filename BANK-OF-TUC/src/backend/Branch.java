package backend;

public class Branch {
	
	private static final String defaultBankCode = "021"; // κωδικός τράπεζας Bank of TUC
	private static final String defaultBranchCode = "0021"; // κωδικός καταστήματος (υποκαταστήματος) Main Branch
	
	private final String bankCode;
    private final String branchCode;

    public Branch(String bankCode, String branchCode) {
        this.bankCode = bankCode;
        this.branchCode = branchCode;
    }

    public String getBankCode() {
        return bankCode;
    }

    public String getBranchCode() {
        return branchCode;
    }
	
    public static Branch getDefaultBranch() {	//οταν δημιουργειται λογαριασμος απο τον πελατη παίρνει αυτό το default branch
	    return new Branch(defaultBankCode, defaultBranchCode);
	}
}
