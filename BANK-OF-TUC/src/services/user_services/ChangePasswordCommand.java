package services.user_services;

import backend.BankSystem;
import backend.FileLogger;
import backend.PasswordHasher;
import backend.users.User;
import services.Command;
import types.LogCategory;
import types.LogLevel;

public class ChangePasswordCommand implements Command {
    
    private BankSystem bankSystem;
    private User user;
    private String oldPassword;
    private String newPassword;

    public ChangePasswordCommand(User user, String newPassword) {
        this.bankSystem = BankSystem.getInstance();
        this.user = user;
        this.newPassword = newPassword;
        // Αποθηκεύουμε το παλιό password για να μπορούμε να κάνουμε undo
        this.oldPassword = user.getPassword();
    }

    @Override
    public void execute() {
        // Ενημέρωση του password στο αντικείμενο του χρήστη
    	FileLogger logger = FileLogger.getInstance();
    	String hashedPassword="";
    	try {
    		hashedPassword=PasswordHasher.hash(this.newPassword);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        user.setPassword(hashedPassword);
        // Αποθήκευση των αλλαγών στο αρχείο/βάση
        bankSystem.saveAllData();
        logger.log(LogLevel.INFO,LogCategory.USER,"Password changed successfully for user: " + user.getUserID());
    }

    @Override
    public void undo() {
       /* // Επαναφορά του παλιού password
        user.setPassword(oldPassword);
        bankSystem.saveAllData();
        System.out.println("Password change undone for user: " + user.getUserID());*/
    }
}