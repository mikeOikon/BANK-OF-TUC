package services;

import backend.FileLogger;
import backend.users.User;
import backend.users.UserBuilder;
import types.LogCategory;
import types.LogLevel;

public class UpdateEmailCommand implements Command {

    private final User user;
    private final String newEmail;
   

    public UpdateEmailCommand(User user, String newEmail) {
        this.user = user;
        this.newEmail = newEmail;
    }

    @Override
    public void execute() {
    	FileLogger logger= FileLogger.getInstance();
        // 1️⃣ Validate using Builder (not mutation yet)
        UserBuilder builder = new UserBuilder()
                .withEmail(newEmail);
        
        
        user.setEmail(builder.getEmail());

        logger.log(LogLevel.INFO,LogCategory.USER," Email updated successfully for UserId."+user.getUserID());
    }

	@Override
	public void undo() {
		// TODO Auto-generated method stub
		
	}
}
