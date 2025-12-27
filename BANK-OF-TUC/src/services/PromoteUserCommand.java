package services;

import backend.BankSystem;
import backend.FileLogger;
import backend.users.*;
import types.LogCategory;
import types.LogLevel;
import types.UserType;

public class PromoteUserCommand implements Command {

    private final BankSystem bankSystem;
    private final User executor;
    private final User target;

    public PromoteUserCommand(BankSystem bankSystem, User executor, User target) {
        this.bankSystem = bankSystem;
        this.executor = executor;
        this.target = target;
    }

    @Override
    public void execute() {
    	
    	FileLogger logger =FileLogger.getInstance();
        if (!executor.canPromoteUser()) {
        	 logger.log(LogLevel.WARNING,LogCategory.USER," UserId:"+executor.getUserID()+" attempted to promote UserId:"+target.getUserID()+" without sufficient permissions.");
            return;
        }

        UserType current = target.getUserType();
        UserType next;

        switch (current) {
            case CUSTOMER:
                next = UserType.EMPLOYEE;
                break;
            case EMPLOYEE:
                next = UserType.ADMIN;
                break;
            default:
            	 logger.log(LogLevel.WARNING,LogCategory.USER," UserId:"+executor.getUserID()+" attempted to promote: "+current);
                return;
        }

        bankSystem.removeUser(target.getUserID());

        UserBuilder builder = new UserBuilder()
                .withUsername(target.getUsername())
                .withPassword(target.getPassword())
                .withName(target.getName())
                .withSurname(target.getSurname())
                .withBranch(target.getBranch())
                .withAFM(target.getAFM());
        
        String UserID=bankSystem.generateId(next);

        User promoted = UserFactory.createUser(
                next,
                UserID,
                builder
        );

        bankSystem.addUser(promoted);

        logger.log(LogLevel.INFO,LogCategory.USER," UserId:"+executor.getUserID()+" promoted to: "+next+ "with new UserId:"+UserID);
    }

	@Override
	public void undo() {
		// TODO Auto-generated method stub
		
	}
    
    
}
