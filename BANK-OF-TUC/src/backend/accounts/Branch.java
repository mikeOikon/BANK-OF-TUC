package backend.accounts;

public class Branch {
	
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
	
}
