package backend.support;

import java.time.LocalDateTime;
import java.util.ArrayList;

import types.TicketStatus;

public class SupportTicket {
	private final String ticketId;
	private final String customerId;
	private  TicketStatus status;
	private final LocalDateTime createdAt;
	private final ArrayList<String> messages;
	
	
	public SupportTicket(String ticketId, String customerId,String initialMessage) {
		this.ticketId = ticketId;
		this.customerId = customerId;
		this.status = TicketStatus.OPEN;
		this.createdAt = LocalDateTime.now();
		this.messages = new ArrayList<>();
		this.messages.add(initialMessage);
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
		this.status=TicketStatus.CLOSED;// Logic to close the ticket
	}
	
	
	public void addMessage(String message) {
		this.messages.add(message);
	}
	
	
	@Override
	public String toString() {
	    return ticketId + " (" + status + ")";
	}

	

}
