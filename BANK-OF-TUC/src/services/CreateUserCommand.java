package services;

import backend.BankSystem;
import backend.users.User;

public class CreateUserCommand implements Command {
	
	private BankSystem bankSystem;
	private User user;
	

	public CreateUserCommand(User user) {
		this.bankSystem = BankSystem.getInstance();
		this.user = user;
		
	}

	@Override
	public void execute() {	
		bankSystem.addUser(user);

	}

	@Override
	public void undo() {
		// TODO Auto-generated method stub

	}

}
