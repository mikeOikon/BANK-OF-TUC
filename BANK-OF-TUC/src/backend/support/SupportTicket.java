package backend.support;

import java.time.LocalDateTime;
import java.util.ArrayList;

import backend.users.User;
import types.TicketStatus;

public class SupportTicket {
	private final String ticketId;
	private final String customerId;
	private  TicketStatus status;
	private final LocalDateTime createdAt;
	private final ArrayList<String> messages;
	private User assignedEmployee; 
	
	
	public SupportTicket(String ticketId, String customerId,String initialMessage) {
		this.ticketId = ticketId;
		this.customerId = customerId;
		this.status = TicketStatus.OPEN;
		this.createdAt = LocalDateTime.now();
		this.messages = new ArrayList<>();
		this.messages.add(initialMessage);
		this.assignedEmployee=null;
	}


	public String getTicketId() {
		return ticketId;
	}


	public String getCustomerId() {
		return customerId;
	}


	public TicketStatus getStatus() {
		return status;
	}


	public LocalDateTime getCreatedAt() {
		return createdAt;
	}


	public ArrayList<String> getMessages() {
		return messages;
	}
	
	public void closeTicket() {
		this.status=TicketStatus.CLOSED;
		this.assignedEmployee=null;
	}
	
	
	public void addMessage(String message) {
		this.messages.add(message);
	}
	
	public void addMessageEmployee(String message, User employee) {
		this.messages.add(message);
		if(this.status==TicketStatus.OPEN) {
			this.status=TicketStatus.IN_PROGRESS;
		}
		this.assignedEmployee=employee;
	}
	
	@Override
	public String toString() {
	    return ticketId + " (" + status + ")";
	}

	

}
