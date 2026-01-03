package backend.users;

import backend.Branch;
//Builder pattern for creating User objects with optional parameters
public class UserBuilder {
   private String username;
   private String password;
   private String email;
   private String name;
   private String surname;
   private String phoneNumber;
   private Branch branch= Branch.getDefaultBranch();
   private String AFM;
   
   
   //specific to business user
   private String buisnessName;
   private String representativeName;
   
   

   public UserBuilder withUsername(String username) {
	  if(username == null || username.length() < 3|| username.length() > 20 || !username.matches("^[a-zA-Z0-9._-]{3,20}$")) {
		   throw new IllegalArgumentException("Invalid username");
	   }
	   this.username = username;
	   return this;}
   
   public UserBuilder withPassword(String password) {
	   this.password = password;
	   return this;}
   
   public UserBuilder withEmail(String email) {
	    if (email == null || !email.matches("^[^@]+@[^@]+\\.[^@]+$")) {
	        throw new IllegalArgumentException("Invalid email");
	    }
	    this.email = email;
	    return this;
	}
   
   public UserBuilder withRepresentativeName(String representativeName) {
	   this.representativeName = representativeName;
	   return this;
   }
   
   public UserBuilder withName(String name) {
	  if(name == null || name.length() < 2|| name.length() > 30 || !name.matches("^[a-zA-ZΑ-Ωα-ωΆΈΉΊΌΎΏάέήίόύώ\\s'-]+$")) {
		   throw new IllegalArgumentException("Invalid name");
	   }
	   this.name = name;
	   return this;}
   
   
   public UserBuilder withSurname(String surname) {
	  if(surname == null || surname.length() < 2|| surname.length() > 30 || !surname.matches("^[a-zA-ZΑ-Ωα-ωΆΈΉΊΌΎΏάέήίόύώ\\s'-]+$")) {
		   throw new IllegalArgumentException("Invalid surname");
	   }
	   this.surname = surname;
	   return this;
   }
   
   public UserBuilder withPhoneNumber(String phoneNumber) {
	    if (phoneNumber == null || !phoneNumber.matches("^[0-9]{10}$")) {
	        throw new IllegalArgumentException("Invalid phone number");
	    }
	    this.phoneNumber = phoneNumber;
	    return this;
	}
	public UserBuilder withAFM(String AFM) {
	   this.AFM = AFM;
	   return this;
   }

   
   public UserBuilder withBranch(Branch branch) {
	   this.branch = branch;
	   return this;
   }
   
   
   public UserBuilder withBusinessName(String businessName) {
	   this.buisnessName = businessName;
	   return this;
   }
   
   


public String getAFM() {
	return AFM;
}

public String getUsername() {
	return username;
}

public String getPassword() {
	return password;
}

public String getEmail() {
	return email;
}

public String getName() {
	return name;
}

public String getSurname() {
	return surname;
}

public String getPhoneNumber() {
	return phoneNumber;
}

public Branch getBranch() {
	return branch;
}

public String getBuisnessName() {
	return buisnessName;
}

public String getRepresentativeName() {
	return representativeName;
}
  
}
