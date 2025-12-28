package behaviors;

public class AdminBehavior implements UserBehavior {

	@Override
	public boolean canRemoveUsers() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean canViewAllAccounts() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean canViewAccounts() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean canTransferMoney() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean canViewTransactionsHistory() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean canViewAllTransactionsHistory() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean canCreateAuditor() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean canPromoteUser() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean canDemoteUser() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean canAssistUsers() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean canOpenTicket() {
		// TODO Auto-generated method stub
		return false;
	}

}
