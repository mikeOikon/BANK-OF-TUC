package backend.users;

import types.UserType;

public class UserFactory {
	
	public static User createUser(UserType userType, String userID,UserBuilder builder){
		switch(userType) {
			case CUSTOMER:
				return new Customer(userID, builder.getUsername(), builder.getPassword(), 
						builder.getEmail(), builder.getName(),
						builder.getSurname(), builder.getPhoneNumber(),
						builder.getBranch());
				
			case EMPLOYEE:
				return new BankEmployer(userID, builder.getUsername(), 
						builder.getPassword(), builder.getEmail(), 
						builder.getName(), builder.getSurname(),
						builder.getPhoneNumber(), builder.getBranch());
			case ADMIN:
				return new Admin(userID, builder.getUsername(), 
						builder.getPassword(), builder.getEmail(), builder.getName(),
						builder.getSurname(), builder.getPhoneNumber(),
						builder.getBranch());
			case BUSINESSCUSTOMER:
				return new ΒusinessCustomer(userID, builder.getUsername(), builder.getPassword(),
						builder.getEmail(),
						 builder.getPhoneNumber(), builder.getBranch(),
						builder.getBuisnessName(),builder.getRepresentativeName());
			default:
				throw new IllegalArgumentException("Invalid user type: " + userType);
		}
	}
		

}
