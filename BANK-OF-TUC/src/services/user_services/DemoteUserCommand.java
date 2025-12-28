package services.user_services;

import backend.BankSystem;
import backend.FileLogger;
import backend.users.*;
import services.Command;
import types.LogCategory;
import types.LogLevel;
import types.UserType;

public class DemoteUserCommand implements Command {

    private final BankSystem bankSystem;
    private final User executor;
    private final User target;

    public DemoteUserCommand(BankSystem bankSystem, User executor, User target) {
        this.bankSystem = bankSystem;
        this.executor = executor;
        this.target = target;
    }

    @Override
    public void execute() {
    	
   	 FileLogger logger= FileLogger.getInstance();

        // 🔐 Permission check via Bridge
        if (!executor.canDemoteUser()) {
        	 logger.log(LogLevel.WARNING,LogCategory.USER," UserId:"+executor.getUserID()+" attempted to demote UserId:"+target.getUserID()+" without sufficient permissions.");
            return;
        }

        UserType current = target.getUserType();
        UserType next;

        switch (current) {
            case ADMIN:
                next = UserType.EMPLOYEE;
                break;
            case EMPLOYEE:
                next = UserType.CUSTOMER;
                break;
            default:
            	logger.log(LogLevel.WARNING,LogCategory.USER," UserId:"+executor.getUserID()+" attempted to demote: "+current);
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
        User demoted = UserFactory.createUser(
                next,
                UserID,
                builder
        );

        bankSystem.addUser(demoted);

        logger.log(LogLevel.INFO,LogCategory.USER," UserId:"+executor.getUserID()+" demoted to: "+next+ "with new UserId:"+UserID);
    }

	@Override
	public void undo() {
		// TODO Auto-generated method stub
		
	}
    
    
}
