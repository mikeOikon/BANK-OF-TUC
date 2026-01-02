package backend;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
	
import com.google.gson.Gson;


import backend.accounts.Account;
import backend.accounts.AccountFactory;
import backend.support.AutoPayManager;
import backend.support.Bill;
import backend.support.InterestManager;
import backend.support.MonthlySubscription;
import backend.support.SupportTicket;
import backend.users.Admin;
import backend.users.Auditor;
import backend.users.BankEmployer;
import backend.users.BusinessCustomer;
import backend.users.Customer;
import backend.users.User;
import backend.users.UserBuilder;
import backend.users.UserFactory;
import behaviors.AdminBehavior;
import behaviors.AuditorBehavior;
import behaviors.BusinessBehavior;
import behaviors.CustomerBehavior;
import behaviors.EmployeeBehavior;
import services.UserManager;
import services.Command;
import types.LogCategory;
import types.LogLevel;
import types.TicketStatus;
import types.UserType;
//import jdk.internal.org.jline.terminal.TerminalBuilder.SystemOutput;
import services.user_services.CreateUserCommand;
import backend.support.MonthlyTaskScheduler;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class BankSystem {
	
	private static volatile BankSystem instance;
	private Map<String, Branch> branches;
	private Map<String,BusinessCustomer> businessCustomers; // Map to store accounts with IBAN as key and account informations as value
	private Map<String,Admin> admins; // Map to store admins with userID as key and informations as value
	private Map<String,Customer> customers; // Map to store customers with userID as key and informations as value
	private Map<String,BankEmployer> bankEmployers; // Map to bankEmployers users with userID as key and informations as value
	private Map<String,Auditor> auditors; // Map to store auditors with userID as key and informations as value
	private BankAccount bankAccount; // η τράπεζα έχει και έναν λογαριασμό για τις προμήθειες κλπ
	private transient UserManager userManager;
	private transient Map<String, Map<String, ? extends User>> userMaps;
	private transient Map<String,User> usersByUsername; // Map to find users by username during login
	private transient Map<String, Integer> loginAttempts;
	private transient Map<String, Long> loginLockedUntil;
	private static final int MAX_LOGIN_ATTEMPTS = 3;
	private static final long LOGIN_LOCK_MS = 60_000L; // 1 minute
	private final Map<String,SupportTicket> tickets=new HashMap<>();
	private final Map<String,Bill> allBills=new HashMap<>();
	private List<MonthlySubscription> subscriptions;


	private  int adminCount = 0;
	private  int customerCount = 0;
	private  int employeeCount = 0;
	private  int auditorCount = 0;
	private String realBaseIso;
	private String simulatedBaseIso;

    public static BankSystemDAO dao = new FileBankSystemDAO();
 
    

	private BankSystem() {
		
		this.admins=new HashMap<>();
		this.customers=new HashMap<>();
		this.bankEmployers=new HashMap<>();
		this.auditors=new HashMap<>();		
		this.businessCustomers=new HashMap<>();
		this.bankAccount = new BankAccount("BANK001", Branch.getDefaultBranch()); //default bank account(TUC)
		this.userManager=new UserManager();
		this.userMaps = new HashMap<>();
		this.subscriptions = new ArrayList<>();
		  userMaps.put("ADM", admins);
		  userMaps.put("CUS", customers);
		  userMaps.put("EMP", bankEmployers);
		  userMaps.put("AUD", auditors);
		  userMaps.put("BUS", businessCustomers);
		this.usersByUsername = new HashMap<>();
		//AccountFactory accountFactory = new AccountFactory();
	}
	
	public static BankSystem getInstance() {
	    if (instance == null) {
	        synchronized (BankSystem.class) {
	            if (instance == null) {
	                instance = dao.load();
	            }
	        }
	    }
	    return instance;
	}
	
	public BankAccount getBankAccount() {
		return bankAccount;
	}
	
	public User getUserById(String userId) {
	    String prefix = userId.substring(0, 3);
	    Map<String, ? extends User> userMap = userMaps.get(prefix);
	    if (userMap != null) {
	        return userMap.get(userId);
	    }
	    return null; // User not found
	}
	// ----------------- Simulated Time API -----------------

	public void initTimeIfMissing() {
		LocalDateTime now = LocalDateTime.now();
		if (realBaseIso == null || realBaseIso.isBlank()) {
			realBaseIso = now.toString();
		}
		if (simulatedBaseIso == null || simulatedBaseIso.isBlank()) {
			simulatedBaseIso = now.toString();
		}
	}

	/** Το "τώρα" του συστήματος που μπορεί να είναι real ή time-lapse. */
	public LocalDateTime getSimulatedNow() {
		initTimeIfMissing();

		LocalDateTime realBase = LocalDateTime.parse(realBaseIso);
		LocalDateTime simulatedBase = LocalDateTime.parse(simulatedBaseIso);

		Duration delta = Duration.between(realBase, LocalDateTime.now());
		return simulatedBase.plus(delta);
	}

	public LocalDate getSimulatedToday() {
		return getSimulatedNow().toLocalDate();
	}

	/** Εσωτερικά: κρατάμε τη simulated ώρα εκεί που θέλουμε και ξαναδένουμε με real clock. */
	private void rebaseTo(LocalDateTime newSimulatedBase) {
		realBaseIso = LocalDateTime.now().toString();
		simulatedBaseIso = newSimulatedBase.toString();
	}

	public void advanceDays(int days) {
		rebaseTo(getSimulatedNow().plusDays(days));
	}

	public void advanceMonths(int months) {

	    for (int i = 0; i < months; i++) {

	        rebaseTo(
	            getSimulatedNow()
	                .plusMonths(1)
	                .withDayOfMonth(1)
	        );

	        for (MonthlySubscription sub : subscriptions) {
	            sub.generateMonthlyBill(this);
	        }

	        MonthlyTaskScheduler scheduler = new MonthlyTaskScheduler();
	        scheduler.addTask(() -> new AutoPayManager(this).processAutoPayments());
	        scheduler.addTask(() -> new InterestManager(this).applyMonthlyInterest());
	        scheduler.executeTasks();
	    }

	    dao.save(this);
	}


	public void setSimulatedTo(LocalDateTime target) {
		if (target != null) rebaseTo(target);
	}

	/** Επιστροφή στην πραγματική ώρα (undo time-lapse). */
	public void resetToRealTime() {
		LocalDateTime now = LocalDateTime.now();
		realBaseIso = now.toString();
		simulatedBaseIso = now.toString();
	}


	void createDefaultAdminIfMissing() {
	    if (!admins.isEmpty()) return;

	    UserBuilder builder = new UserBuilder();
	    try {
	        builder.withUsername("admin")
	               .withPassword(PasswordHasher.hash("Admin123"))
	               .withEmail("admin@bank.com")
	               .withName("System")
	               .withSurname("Admin")
	               .withPhoneNumber("0000000000")
	               .withBranch(Branch.getDefaultBranch());
	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    User admin = UserFactory.createUser(
	        UserType.ADMIN,
	        generateId(UserType.ADMIN),
	        builder
	    );

	    addUser(admin);
	}


	
	void rebuildTransientState() {

	    this.userMaps = new HashMap<>();
	    userMaps.put("ADM", admins);
	    userMaps.put("CUS", customers);
	    userMaps.put("EMP", bankEmployers);
	    userMaps.put("AUD", auditors);
	    userMaps.put("BUS", businessCustomers);

	    this.usersByUsername = new HashMap<>();

	    for (User u : admins.values()) usersByUsername.put(u.getUsername(), u);
	    for (User u : customers.values()) usersByUsername.put(u.getUsername(), u);
	    for (User u : bankEmployers.values()) usersByUsername.put(u.getUsername(), u);
	    for (User u : auditors.values()) usersByUsername.put(u.getUsername(), u);
	    for (User u : businessCustomers.values()) usersByUsername.put(u.getUsername(), u);

	    for (Admin a : admins.values()) a.setBehavior(new AdminBehavior());
	    for (Customer c : customers.values()) c.setBehavior(new CustomerBehavior());
	    for (Auditor a : auditors.values()) a.setBehavior(new AuditorBehavior());
	    for (BankEmployer e : bankEmployers.values()) e.setBehavior(new EmployeeBehavior());
	    for (BusinessCustomer b : businessCustomers.values()) b.setBehavior(new BusinessBehavior());
		initTimeIfMissing();
		// init login lock maps
		this.loginAttempts = new HashMap<>();
		this.loginLockedUntil = new HashMap<>();
	}


	
	static BankSystem createEmptySystem() {
	    return new BankSystem();
	}


	//να γινεται save οταν γινεται καποια αλλαγη και οταν κλεινει το προγραμμα
	 public void shutdown() {
	    dao.save(this);
	    FileLogger logger= FileLogger.getInstance();
	    logger.log(LogLevel.INFO,LogCategory.SYSTEM,"System shutting down, data persisted.");
	 }
	public Branch getBranch(String branchCode) {
        return branches.get(branchCode);
    }
	
	public void addUser(User user) {
		switch(user.getUserType()) {
			case ADMIN:
				this.admins.put(user.getUserID(), (Admin) user);
				break;
			case CUSTOMER:
				this.customers.put(user.getUserID(), (Customer) user);
				break;
			case EMPLOYEE:
				this.bankEmployers.put(user.getUserID(), (BankEmployer) user);
				break;
			case AUDITOR:
				this.auditors.put(user.getUserID(), (Auditor) user);
				break;
			case BUSINESSCUSTOMER:
				this.businessCustomers.put(user.getUserID(), (BusinessCustomer) user);
				break;
			default:
				throw new IllegalArgumentException("Invalid user type: " + user.getUserType());
		}
		
		usersByUsername.put(user.getUsername(), user);
	}
	
	
	
	public void removeUser(String userId) {
	String prefix = userId.substring(0, 3);
	Map<String, ? extends User> userMap = userMaps.get(prefix);
	FileLogger logger= FileLogger.getInstance();

	if (userMap != null) {	
		User removed= userMap.remove(userId);
		if (removed != null) {
			usersByUsername.remove(removed.getUsername());	
			//System.out.println("User " + userId + " removed successfully.");
		} else {
			logger.log(LogLevel.WARNING,LogCategory.USER,"User " + userId + " not found.");
		}
	} else {
		logger.log(LogLevel.ERROR,LogCategory.USER,"Invalid user ID prefix: " + prefix);
	}
		}
	
	
	public User createUser(UserType type, UserBuilder builder) {
		String userID = generateId(type);
		User newUser = UserFactory.createUser(type,userID,builder);//create user
		Command create= new CreateUserCommand(newUser);				
		userManager.execute(create);
		return newUser;
	}

	public Map<String, Customer> getCustomers() {
	    return customers;
	}

	public Map<String, BusinessCustomer> getBusinessCustomers() {
	    return businessCustomers;
	}

	public Map<String, Admin> getAdmins() {
	    return admins;
	}
	
	public Map<String, BankEmployer> getBankEmployers() {
	    return bankEmployers;
	}
	
	public Map<String, Auditor> getAuditors() {
	    return auditors;
	}
	
	


	public String generateId(UserType type) {  //Genrates unique ID for each user ids are in order
	    String prefix;
	    if (type == UserType.ADMIN) {
	        prefix = "ADM";
	        return String.format("%s%03d", prefix, ++adminCount);
	    } else if (type == UserType.CUSTOMER) {
	        prefix = "CUS";
	        return String.format("%s%03d", prefix, ++customerCount);
	    } else if (type == UserType.EMPLOYEE) {
	        prefix = "EMP";
	        return String.format("%s%03d", prefix, ++employeeCount);
	    } else if (type == UserType.AUDITOR) {
	        prefix = "AUD";
	        return String.format("%s%03d", prefix, ++auditorCount);
	    } else if (type == UserType.BUSINESSCUSTOMER) {
	        prefix = "BUS";
	        return String.format("%s%03d", prefix, ++customerCount); //business customer count is same as customer count
	    } else {
	        throw new IllegalArgumentException("Unknown user type");
	    }

	   
	}
	public boolean isUserLocked(String username) {
		if (loginLockedUntil == null) loginLockedUntil = new HashMap<>();
		Long until = loginLockedUntil.get(username);
		return until != null && System.currentTimeMillis() < until;
	}

	public long remainingLockSeconds(String username) {
		if (loginLockedUntil == null) loginLockedUntil = new HashMap<>();
		Long until = loginLockedUntil.get(username);
		if (until == null) return 0;
		long diff = until - System.currentTimeMillis();
		return diff > 0 ? (diff + 999) / 1000 : 0;
	}

	/**
	 * @return User αν πετύχει το login, αλλιώς null.
	 * Με 3 λάθος passwords κάνει lock για 60".
	 */
	public User loginUser(String username, String password) {
		if (username == null) return null;

		username = username.trim();

		if (loginAttempts == null) loginAttempts = new HashMap<>();
		if (loginLockedUntil == null) loginLockedUntil = new HashMap<>();

		// locked?
		if (isUserLocked(username)) {
			return null;
		}

		User user = findUserByUsername(username);
		if (user == null) {
			// Προαιρετικά: ΜΗΝ μετράς attempts για άγνωστο username
			return null;
		}

		// verify credentials (με PasswordHasher.verify μέσα στο User.login)
		if (user.login(username, password)) {
			loginAttempts.remove(username);
			loginLockedUntil.remove(username);
			return user;
		}

		// failed attempt
		int attempts = loginAttempts.getOrDefault(username, 0) + 1;
		loginAttempts.put(username, attempts);

		if (attempts >= MAX_LOGIN_ATTEMPTS) {
			loginLockedUntil.put(username, System.currentTimeMillis() + LOGIN_LOCK_MS);
			loginAttempts.put(username, 0); // reset counter after lock
		}

		return null;
	}


	public Account getAccountbyNumber(String accountNumber) {	//method to find account by account number (used in transfer money)
		for(User user : customers.values()) {
			Customer customer = (Customer) user;
			Account foundAccount = customer.findAccountByNumber(accountNumber);
			if(foundAccount != null)
				return foundAccount;
		}
		for(User user : businessCustomers.values()) {
			BusinessCustomer bCustomer = (BusinessCustomer) user;
			Account foundAccount = bCustomer.findAccountByNumber(accountNumber);
			if(foundAccount != null)
				return foundAccount;
		}
		return null; // Account not found
	}
	

	//mathod to get all accounts in the bank system (used by auditor) 
	public List<Account> getAllAccounts() {
		List<Account> allAccounts = new ArrayList<>();
		for(User user : customers.values()) {
			Customer customer = (Customer) user;
			allAccounts.addAll(customer.getAccounts());
		}
		return List.copyOf(allAccounts);
	}  
	
	
	public String generateTicketID() {
		return "TIC-"+System.currentTimeMillis();
	}
	
	public void addTicket(SupportTicket ticket) {
		tickets.put(ticket.getTicketId(),ticket);
	}
	
	public Collection<SupportTicket> getAllTickets(){
		return tickets.values();
	}

	
	public SupportTicket getTicketForCustomer(String customerId) {
	    for (SupportTicket ticket : tickets.values()) {
	        if (ticket.getCustomerId().equals(customerId) && (ticket.getStatus() == TicketStatus.OPEN || 
	        		ticket.getStatus() == TicketStatus.IN_PROGRESS)) {
	            return ticket;
	        }
	    }
	    return null;
	}

	
	public User findUserByUsername(String username) {
	    return usersByUsername.get(username);
	}
	
	public void addBill(Bill bill) {
        if (bill == null || bill.getPaymentCode() == null) {
            return;
        }
        allBills.put(bill.getPaymentCode(), bill);
        
        // Προαιρετικά: αποθήκευση αμέσως μετά την προσθήκη
        // saveAllData(); 
    }
	
	public Bill findBillByCode(String code) {
        return allBills.get(code);
    }
	
	public void removeBill(String code) {
        allBills.remove(code);
    }
	
	public void saveAllData() {
	    dao.save(this);
	}

	public List<Bill> getAllBills() {
	    return new ArrayList<>(allBills.values());
	}
	
	public void enableMonthlyAutoPay(String subscriptionId, String accountIBAN) {
        if (subscriptionId == null || accountIBAN == null) return;

        for (MonthlySubscription sub : subscriptions) {
            if (sub.getSubscriptionId().equals(subscriptionId)) {
                sub.enableAutoPay(accountIBAN);
                System.out.printf("BankSystem: AutoPay ενεργοποιήθηκε για subscription %s στον λογαριασμό %s%n",
                        subscriptionId, accountIBAN);
                break;
            }
        }
    }
	
	public void addSubscription(MonthlySubscription sub) {
	    if (sub == null) return;
	    
	    // Αρχικοποίηση αν για κάποιο λόγο είναι null (ασφάλεια)
	    if (this.subscriptions == null) {
	        this.subscriptions = new ArrayList<>();
	    }
	    
	    this.subscriptions.add(sub);
	    
	    // Προαιρετικά: Log την ενέργεια
	    FileLogger.getInstance().log(LogLevel.INFO, LogCategory.SYSTEM, 
	        "New Monthly Subscription added: " + sub.getSubscriptionId());
	}
	
	public void disableMonthlyAutoPay(String subscriptionId) {
	    for (MonthlySubscription sub : subscriptions) {
	        if (sub.getSubscriptionId().equals(subscriptionId)) {
	            sub.disableAutoPay();
	            break;
	        }
	    }
	}

	public String getActiveAutoPayIBAN(String subscriptionId) {
	    for (MonthlySubscription sub : subscriptions) {
	        if (sub.getSubscriptionId().equals(subscriptionId)) {
	            return sub.getAutoPayAccountIBAN(); // null αν δεν υπάρχει ενεργή πάγια
	        }
	    }
	    return null;
	}

	
}

