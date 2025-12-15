package services;

import backend.BankSystem;
import backend.users.User;
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
		  if (!executor.canRemoveUsers()) {
	            throw new SecurityException("Only admins can remove users.");
	        }

	        bankSystem.removeUser(userId);
	        System.out.println("User " + userId + " removed by admin " + executor.getUsername());
	    }

	

	@Override
	public void undo() {
		// TODO Auto-generated method stub

	}

}
