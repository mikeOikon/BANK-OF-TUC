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
}
