package services;

import backend.users.User;
import backend.users.UserBuilder;

public class UpdateEmailCommand implements Command {

    private final User user;
    private final String newEmail;

    public UpdateEmailCommand(User user, String newEmail) {
        this.user = user;
        this.newEmail = newEmail;
    }

    @Override
    public void execute() {

        // 1️⃣ Validate using Builder (not mutation yet)
        UserBuilder builder = new UserBuilder()
                .withEmail(newEmail);

        // 2️⃣ Apply change safely
        user.setEmail(builder.getEmail());

        System.out.println("✅ Email updated successfully.");
    }

	@Override
	public void undo() {
		// TODO Auto-generated method stub
		
	}
}
