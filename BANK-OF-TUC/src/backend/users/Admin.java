package backend.users;

import java.util.Scanner;

import backend.BankSystem;
import backend.Branch;
import backend.accounts.Account;
import behaviors.AdminBehavior;
import behaviors.UserBehavior;
import types.UserType;

public class Admin extends User {
	private static int customerCount = 0;
    private static int adminCount = 0;
    private static int employeeCount = 0;
    private static int auditorCount = 0;
   
    
    //ισως ο admin δεν πρεπει να εχει branch
	public Admin(String userID, String username,String password, String name, String surname, Branch branch,String AFM) {
		super(userID, username, password, name, surname, branch,AFM);
		this.userBehavior = new AdminBehavior();
	}
	
	public boolean promoteUser(User oldUser, String newRole) {
	    BankSystem bank = BankSystem.getInstance();
	    
	    // 1. Μετατροπή του String σε UserType enum
	    UserType targetType;
	    try {
	        targetType = UserType.valueOf(newRole.toUpperCase());
	    } catch (IllegalArgumentException e) {
	        System.out.println("Invalid role specified for promotion: " + newRole);
	        return false;
	    }

	    // 2. Προετοιμασία του Builder με τα στοιχεία του παλιού χρήστη
	    UserBuilder builder = new UserBuilder();
	    try {
	        builder.withUsername(oldUser.getUsername())
	               .withPassword(oldUser.getPassword()) // Διατηρεί το hashed password
	               .withName(oldUser.getName())
	               .withSurname(oldUser.getSurname())
	               .withBranch(oldUser.getBranch())
	               .withAFM(oldUser.getAFM());
	    } catch (Exception e) {
	        e.printStackTrace();
	        return false;
	    }

	    // 4. Χρήση του BankSystem για δημιουργία (που καλεί την UserFactory & generateID)
	    User newUser = bank.createUser(targetType, builder);
	    
	    removeUser(oldUser.getUserID());
	    
	    bank.saveAllData();
	    return true;
	}

	public boolean removeUser(String userId) {
		BankSystem bank = BankSystem.getInstance();
		bank.removeUser(userId);
		return true;
	}

	//na doume ti prepei na einai protected
	//ο αδμιν μπορει να κανει admin ή employer αλλον user (εφοσον αυτος δεν ειναι business customer)
	
	public UserType getUserType() {
		return UserType.ADMIN;
	}
	
	protected void manageUserAccounts() {
		
	}
	
	public boolean deleteUserAccount(User user, String IBAN) {
	    if (user == null) {
	        return false;
	    }

	    if (user instanceof Customer) {
	    	Customer customer = (Customer) user;
	        Account accToClose = customer.findAccountByNumber(IBAN);
	        if (accToClose != null) {
	            customer.getAccounts().remove(accToClose);
	            return true;
	        }
	    }
	    else if(user instanceof BusinessCustomer) {
	    	BusinessCustomer bCustomer = (BusinessCustomer) user;
	        Account accToClose = bCustomer.findAccountByNumber(IBAN);
	        if (accToClose != null) {
	            bCustomer.getAccounts().remove(accToClose);
	            return true;
	        }
	    }
	    return false;
	}
	
	
	protected void updateCustomerInformation() {

	}
	
	protected void updateCustomerAccountDetails() {
		//πχ 
	}
	public void setUserId(String userID) {
		this.userID = userID;
	}
	
	public String getFullName() {
	    return this.name + " " + this.surname;
	}
}
