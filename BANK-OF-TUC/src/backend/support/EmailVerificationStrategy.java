package backend.support;

import javax.swing.JOptionPane;

import backend.users.User;

public class EmailVerificationStrategy implements VerificationStrategy {
    
	
	@Override
    public String sendCode(User user) {
        String code = generateCode();
        JOptionPane.showMessageDialog(null," EMAIL TO: " + user.getEmail() +"\nYour verification code is: " + code,
            "Email Verification",
            JOptionPane.INFORMATION_MESSAGE
        );
        return code;
    }

    private String generateCode() {
        return String.valueOf((int)(Math.random() * 900000 + 100000));
    }


}
