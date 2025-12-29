package backend.support;

import backend.users.User;

public class VerificationContext {
    private VerificationStrategy strategy;
    private String generatedCode;

    public void setStrategy(VerificationStrategy strategy) {
        this.strategy = strategy;
    }

    public void send(User user) {
        generatedCode = strategy.sendCode(user);
    }

    public boolean verify(String inputCode) {
        return generatedCode.equals(inputCode);
    }
}
