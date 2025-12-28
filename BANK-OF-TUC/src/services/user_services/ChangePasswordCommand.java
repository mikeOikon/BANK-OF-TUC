package services.user_services;

import backend.BankSystem;
import backend.users.User;
import services.Command;

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
        user.setPassword(newPassword);
        // Αποθήκευση των αλλαγών στο αρχείο/βάση
        bankSystem.saveAllData();
        System.out.println("Password changed successfully for user: " + user.getUserID());
    }

    @Override
    public void undo() {
        // Επαναφορά του παλιού password
        user.setPassword(oldPassword);
        bankSystem.saveAllData();
        System.out.println("Password change undone for user: " + user.getUserID());
    }
}