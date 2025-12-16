package services;

import backend.BankSystem;
import backend.users.*;
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

        // 🔐 Permission check via Bridge
        if (!executor.canPromoteUser()) {
            System.out.println("❌ Permission denied. Only admins can promote users.");
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
                System.out.println("❌ Cannot promote user of type " + current);
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

        User promoted = UserFactory.createUser(
                next,
                bankSystem.generateId(next),
                builder
        );

        bankSystem.addUser(promoted);

        System.out.println("✅ User promoted to " + next);
    }

	@Override
	public void undo() {
		// TODO Auto-generated method stub
		
	}
    
    
}
