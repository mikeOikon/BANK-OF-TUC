package services.user_services;

import backend.BankSystem;
import backend.FileLogger;
import backend.users.User;
import services.Command;
import types.LogCategory;
import types.LogLevel;
import types.UserType;

public class DeleteUserCommand implements Command {
	
	 private BankSystem bankSystem;
	 private String userId;
	 private User executor;


	    public DeleteUserCommand(User executor,String userId) {
	        this.bankSystem = BankSystem.getInstance();
	        this.executor = executor;
	        this.userId = userId;
	    }

	@Override
	public void execute() {
		 FileLogger logger= FileLogger.getInstance();
		  if (!executor.canRemoveUsers()) {
	            throw new SecurityException("Only admins can remove users.");
	        }

	        bankSystem.removeUser(userId);
	        logger.log(LogLevel.INFO,LogCategory.USER,"User " + userId + " removed by admin " + executor.getUsername());
	    }

	

	@Override
	public void undo() {
		// TODO Auto-generated method stub

	}

}
