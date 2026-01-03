package services.account_services;

import backend.BankSystem;
import backend.accounts.Account;
import backend.accounts.BusinessAccount;
import backend.support.Bill;
import services.Command;
import types.TransactionType;

public class PayBillCommand implements Command {

    private final Account userAccount;  // Ο λογαριασμός του πελάτη που πληρώνει
    private final Bill bill;            // Ο λογαριασμός (το "χαρτί") της πληρωμής
    private final BankSystem bankSystem;
    private BusinessAccount businessAccount;    // Θα βρεθεί κατά την εκτέλεση

    public PayBillCommand(Account userAccount, Bill bill) {
        this.userAccount = userAccount;
        this.bill = bill;
        this.bankSystem = BankSystem.getInstance();
    }

    @Override
    public void execute() {
        // 1. Βρίσκουμε τον λογαριασμό της επιχείρησης μέσω του IBAN που αναγράφει το Bill
        businessAccount = (BusinessAccount)bankSystem.getAccountbyNumber(bill.getBusinessIBAN());
        System.out.println("Attempting to pay bill " + bill.getPaymentCode() + " from " + userAccount.getIBAN() + " to " + bill.getBusinessIBAN());
        if (businessAccount == null) {
            System.err.println("Error: Business account not found for IBAN: " + bill.getBusinessIBAN());
            return;
        }

        try {
            // 2. Χρησιμοποιούμε την transferTo που υλοποιήσαμε στην Account.
            // Αυτή η μέθοδος θα:
            // - Ελέγξει υπόλοιπο & frozen status
            // - Αλλάξει τα balances και των δύο
            // - Δημιουργήσει το TransferTransaction record
            // - Προσθέσει το record στα Transactions και των δύο λογαριασμών
        	boolean success = bankSystem.billTransaction(TransactionType.BILL_PAYMENT, userAccount, bill);

            if (success) {
                // 3. Μαρκάρουμε το Bill ως πληρωμένο στο σύστημα
            	 //userAccount.markBillAsIssued(bill);
            	 
            	 bankSystem.saveAllData();
            	 System.out.println("Bill " + bill.getPaymentCode() + " paid successfully.");
            }
        } catch (Exception e) {
            // Εδώ θα πιάσει IllegalStateException αν ο λογαριασμός είναι frozen 
            // ή IllegalArgumentException αν δεν φτάνουν τα χρήματα
            System.err.println("Payment failed: " + e.getMessage());
        }
    }
    
    @Override 
    public void undo() {
		// Αντιστροφή: Η επιχείρηση επιστρέφει τα χρήματα στον χρήστη
		
	}
	
}
