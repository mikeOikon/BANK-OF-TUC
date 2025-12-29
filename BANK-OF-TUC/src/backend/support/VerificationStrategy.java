package backend.support;

import backend.users.User;

public interface VerificationStrategy {
	
	String sendCode(User user);

}
