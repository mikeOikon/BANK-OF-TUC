package backend;

public abstract class User {

	private String userID;
	private String password;
	
	public User(String userID, String password) {
		this.userID = userID;
		this.password = password;
		//this.email = email;
	}
	
	//na doume poies methodoi prepei na einai protected
	
	public String getUserID() {
		return userID;
	}
	private String getPassword() {
		return password;
	}
	
	protected boolean login(String userID, String password) {
		return this.userID.equals(userID) && this.password.equals(password);
	}
	
	protected void logout() {
		// Implement logout functionality
	}
	
	protected void changePassword(String newPassword) {
		this.password = newPassword;
	}
	
	//protected void resetPassword() {
		// Implement reset password functionality
	
	//}
	
	protected void deleteAccount() {
		// Implement delete account functionality
	}
	
}
