package services;

import backend.BankSystem;
import backend.users.*;
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

        // 🔐 Permission check via Bridge
        if (!executor.canDemoteUser()) {
            System.out.println("❌ Permission denied. Only admins can demote users.");
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
                System.out.println("❌ Cannot demote user of type " + current);
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

        User demoted = UserFactory.createUser(
                next,
                bankSystem.generateId(next),
                builder
        );

        bankSystem.addUser(demoted);

        System.out.println("✅ User demoted to " + next);
    }

	@Override
	public void undo() {
		// TODO Auto-generated method stub
		
	}
    
    
}
