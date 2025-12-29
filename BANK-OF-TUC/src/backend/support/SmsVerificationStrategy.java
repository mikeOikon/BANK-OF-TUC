
package backend.support;

import javax.swing.JOptionPane;

import backend.users.User;


public class SmsVerificationStrategy implements VerificationStrategy {
    @Override
    public String sendCode(User user) {
        String code = generateCode();
        JOptionPane.showMessageDialog(null,
            "SMS TO: " + user.getPhoneNumber() +"\nYour verification code is: " + code,"SMS Verification",
            JOptionPane.INFORMATION_MESSAGE
        );
        return code;
    }

    private String generateCode() {
        return String.valueOf((int)(Math.random() * 900000 + 100000));
    }
}
