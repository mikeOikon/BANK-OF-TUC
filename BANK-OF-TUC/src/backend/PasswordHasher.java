package backend;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class PasswordHasher {
	
	 public static String hash(String password) throws Exception {
	        MessageDigest md = MessageDigest.getInstance("SHA-256");
	        byte[] bytes = md.digest(password.getBytes(StandardCharsets.UTF_8));
	        StringBuilder sb = new StringBuilder();
	        for (byte b : bytes) {
	            sb.append(String.format("%02x", b));
	        }
	        return sb.toString();
	    }

	public static boolean verify(String password, String password2) {
		try {
			String hashedPassword = hash(password);
			return hashedPassword.equals(password2);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

}
