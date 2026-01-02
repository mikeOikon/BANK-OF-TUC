package frontend.gui;

import javax.swing.JOptionPane;
import javax.swing.JPasswordField;

import backend.BankSystem;
import backend.support.EmailVerificationStrategy;
import backend.support.SmsVerificationStrategy;
import backend.support.VerificationContext;
import backend.users.User;
import services.UserManager;
import services.user_services.ChangePasswordCommand;

public class ForgotPasswordController {

    public void startRecovery(String username) {
        User user = BankSystem.getInstance().findUserByUsername(username);

        if (user == null) {
            JOptionPane.showMessageDialog(null, "User not found");
            return;
        }

        if (user.getEmail() == null && user.getPhoneNumber() == null) {
            JOptionPane.showMessageDialog(null,
                "Cannot recover password.\nNo email or phone on file.");
            return;
        }

        VerificationContext context = new VerificationContext();

        if (user.getEmail() != null) {
            context.setStrategy(new EmailVerificationStrategy());
        } else {
            context.setStrategy(new SmsVerificationStrategy());
        }

        context.send(user);
        showCodeInput(user, context);
    }

    private void showCodeInput(User user, VerificationContext context) {
        String input = JOptionPane.showInputDialog("Enter verification code:");

        if (input == null) return;

        if (context.verify(input)) {
            showResetPassword(user);
        } else {
            JOptionPane.showMessageDialog(null, "Invalid code");
        }
    }

    private void showResetPassword(User user) {
        JPasswordField p1 = new JPasswordField();
        JPasswordField p2 = new JPasswordField();

        Object[] fields = {
            "New Password:", p1,
            "Confirm Password:", p2
        };

        int result = JOptionPane.showConfirmDialog(
            null, fields, "Reset Password", JOptionPane.OK_CANCEL_OPTION);
        if (newPasswordIsEmpty(p1, p2)) {
            JOptionPane.showMessageDialog(null, "Password cannot be empty");
            return;
        }
        if (newPasswordIsTooShort(p1, p2)) {
            JOptionPane.showMessageDialog(null, "Password must be at least 6 characters long");
            return;
        }if (newPasswordLacksComplexity(p1, p2)) {
            JOptionPane.showMessageDialog(null, "Password must contain at least one uppercase letter, one lowercase letter, and one digit");
            return;
        }
        if (result == JOptionPane.OK_OPTION) {
            if (!String.valueOf(p1.getPassword())
                    .equals(String.valueOf(p2.getPassword()))) {
                JOptionPane.showMessageDialog(null, "Passwords do not match");
                return;
            }

            ChangePasswordCommand changePasswordCommand =
				new ChangePasswordCommand(user, String.valueOf(p1.getPassword()));
            
            UserManager manager = new UserManager();
            manager.execute(changePasswordCommand);
            
            JOptionPane.showMessageDialog(null, "Password successfully changed");
        }
    }

    private boolean newPasswordLacksComplexity(JPasswordField p1, JPasswordField p2) {
        if(!String.valueOf(p1.getPassword()).matches(".*[A-Z].*") || !String.valueOf(p1.getPassword()).matches(".*[a-z].*") || !String.valueOf(p1.getPassword()).matches(".*\\d.*")) {
               return true;
           }
        return false;
    }

    private boolean newPasswordIsTooShort(JPasswordField p1, JPasswordField p2) {
       if(p1.getPassword().length<6) {
           return true;
       }
        return false;
    }

    private boolean newPasswordIsEmpty(JPasswordField p1, JPasswordField p2) {
        if(String.valueOf(p1.getPassword()).isEmpty() || String.valueOf(p2.getPassword()).isEmpty()) {
               return true;
           }
        return false;
    }
}
