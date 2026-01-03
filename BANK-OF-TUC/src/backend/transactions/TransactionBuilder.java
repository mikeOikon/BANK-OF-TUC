package backend.transactions;

import backend.accounts.Account;
import backend.support.Bill;
import types.TransactionType;

/**
 * Builder pattern για δημιουργία Transaction αντικειμένων.
 * Υποστηρίζει όλες τις συναλλαγές: DEPOSIT, WITHDRAW, TRANSFER, BILL_PAYMENT, AUTO_BILL_PAYMENT, INTEREST, FEE.
 */
public class TransactionBuilder {

    private TransactionType type;
    private Account from;
    private Account to;
    private double amount;
    private Bill bill;             // Για Bill Payments
    private String description;    // Για Fee ή άλλες custom περιγραφές

    // ------------------------------
    //       Fluent Setters
    // ------------------------------

    public TransactionBuilder withType(TransactionType type) {
        this.type = type;
        return this;
    }

    public TransactionBuilder withFrom(Account from) {
        this.from = from;
        return this;
    }

    public TransactionBuilder withTo(Account to) {
        this.to = to;
        return this;
    }

    public TransactionBuilder withAmount(double amount) {
        this.amount = amount;
        return this;
    }

    public TransactionBuilder withBill(Bill bill) {
        this.bill = bill;
        return this;
    }

    public TransactionBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

	protected TransactionType getType() {
		return type;
	}

	protected void setType(TransactionType type) {
		this.type = type;
	}

	protected Account getFrom() {
		return from;
	}

	protected void setFrom(Account from) {
		this.from = from;
	}

	protected Account getTo() {
		return to;
	}

	protected void setTo(Account to) {
		this.to = to;
	}

	protected double getAmount() {
		return amount;
	}

	protected void setAmount(double amount) {
		this.amount = amount;
	}

	protected Bill getBill() {
		return bill;
	}

	protected void setBill(Bill bill) {
		this.bill = bill;
	}

	protected String getDescription() {
		return description;
	}

	protected void setDescription(String description) {
		this.description = description;
	}    

}
