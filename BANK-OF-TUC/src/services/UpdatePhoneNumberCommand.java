package services;

import backend.users.User;
import backend.users.UserBuilder;

public class UpdatePhoneNumberCommand implements Command {

	 private final User user;
	    private final String newPhoneNumber;

	    public UpdatePhoneNumberCommand(User user, String newEmail) {
	        this.user = user;
	        this.newPhoneNumber = newEmail;
	    }

	    @Override
	    public void execute() {

	        // 1️⃣ Validate using Builder (not mutation yet)
	        UserBuilder builder = new UserBuilder()
	                .withPhoneNumber(newPhoneNumber);

	        // 2️⃣ Apply change safely
	        user.setPhoneNumber(builder.getPhoneNumber());

	        System.out.println("✅ Email updated successfully.");
	    }
	@Override
	public void undo() {
		// TODO Auto-generated method stub

	}

}
