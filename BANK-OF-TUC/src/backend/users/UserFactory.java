package backend.users;

import types.UserType;

public class UserFactory {
	
	public static User createUser(UserType userType, String userID,UserBuilder builder){
		switch(userType) {
			case CUSTOMER:
				return new Customer(userID, builder.getUsername(), builder.getPassword(), 
						builder.getName(),
						builder.getSurname(), 
						builder.getBranch(),builder.getAFM());
				
			case EMPLOYEE:
				return new BankEmployer(userID, builder.getUsername(), 
						builder.getPassword(), 
						builder.getName(), builder.getSurname(),
						 builder.getBranch(), builder.getAFM());
			case ADMIN:
				return new Admin(userID, builder.getUsername(), 
						builder.getPassword(), builder.getName(),
						 builder.getPhoneNumber(),
						builder.getBranch(), builder.getAFM());
			case BUSINESSCUSTOMER:
				return new ΒusinessCustomer(userID, builder.getUsername(), builder.getPassword(),
						builder.getBranch(),
						builder.getBuisnessName(),
						builder.getRepresentativeName(), builder.getAFM());
			default:
				throw new IllegalArgumentException("Invalid user type: " + userType);
		}
	}
		

}
