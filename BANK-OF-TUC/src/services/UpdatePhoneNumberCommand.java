package services;

import backend.FileLogger;
import backend.users.User;
import backend.users.UserBuilder;
import types.LogCategory;
import types.LogLevel;

public class UpdatePhoneNumberCommand implements Command {

	 private final User user;
	    private final String newPhoneNumber;

	    public UpdatePhoneNumberCommand(User user, String newEmail) {
	        this.user = user;
	        this.newPhoneNumber = newEmail;
	    }

	    @Override
	    public void execute() {
	    	
	    	FileLogger logger= FileLogger.getInstance();

	        // 1️⃣ Validate using Builder (not mutation yet)
	        UserBuilder builder = new UserBuilder()
	                .withPhoneNumber(newPhoneNumber);

	        // 2️⃣ Apply change safely
	        user.setPhoneNumber(builder.getPhoneNumber());

	        logger.log(LogLevel.INFO,LogCategory.USER," Phone Number updated successfully for UserId."+user.getUserID());
	    }
	@Override
	public void undo() {
		// TODO Auto-generated method stub

	}

}
