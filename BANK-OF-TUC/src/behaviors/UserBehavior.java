package behaviors;

public interface UserBehavior {
	 boolean canViewAccounts();
	 boolean canTransferMoney();
	 boolean canViewTransactionsHistory();
	 boolean canRemoveUsers();
	 boolean canViewAllAccounts();
	 boolean canViewAllTransactionsHistory();
	 boolean canCreateAuditor();
	 boolean canPromoteUser();
	 boolean canDemoteUser();
	 boolean canAssistUsers();
	 boolean canOpenTicket();
}
