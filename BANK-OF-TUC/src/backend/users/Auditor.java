package backend.users;

import backend.accounts.Branch;

public class Auditor extends User {

	public Auditor(String userID, String password, String email, String name, String surname, String phoneNumber, Branch branch) {
		super(userID, password, email, name, surname, phoneNumber, branch);
	}

}
