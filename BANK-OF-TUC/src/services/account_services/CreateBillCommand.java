package services.account_services;

import backend.BankSystem;
import backend.support.Bill;
import services.Command;

public class CreateBillCommand implements Command {
    private Bill bill;
    private BankSystem bankSystem;

    public CreateBillCommand(Bill bill) {
        this.bill = bill;
        this.bankSystem = BankSystem.getInstance();
    }

    @Override
    public void execute() {
        // Προσθήκη του bill στη λίστα του συστήματος
        bankSystem.addBill(bill);
        bankSystem.saveAllData();
    }

    @Override
    public void undo() {
        // Αφαίρεση του bill σε περίπτωση αναίρεσης
        bankSystem.removeBill(bill.getPaymentCode());
        bankSystem.saveAllData();
    }
}