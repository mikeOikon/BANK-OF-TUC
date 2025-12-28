package services;

import backend.BankSystem;
import backend.support.SupportTicket;
import backend.users.User;

public class OpenTicketCommand implements Command {
	
	private final User customer;
	private final String message;
	
	

	public OpenTicketCommand(User customer, String message) {
		this.customer = customer;
		this.message = message;
	}
	

	@Override
	public void execute() {
		if(!customer.canOpenTicket()) {
			throw new SecurityException("User not allowed to open Tickets");
			
		}
		
		BankSystem bank= BankSystem.getInstance();
		String ticketId= bank.generateTicketID();
		
		SupportTicket ticket= new SupportTicket(ticketId,customer.getUserID(),message);

	}

	@Override
	public void undo() {
		// TODO Auto-generated method stub

	}

}
