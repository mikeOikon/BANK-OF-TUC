package behaviors;

public class EmployeeBehavior implements UserBehavior {

	@Override
	public boolean canRemoveUsers() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean canViewAllAccounts() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean canAdvanceTime() { return false; }


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
		return false;
	}

	@Override
	public boolean canCreateAuditor() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean canPromoteUser() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean canDemoteUser() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean canAssistUsers() {
		// TODO Auto-generated method stub
		return true;
	}
	
	@Override
	public boolean canPayBills() {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public boolean canIssueBills() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean canOpenTicket() {
		// TODO Auto-generated method stub
		return false;
	}

}
