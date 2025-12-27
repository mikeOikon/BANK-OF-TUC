package services;

import backend.BankSystem;
import backend.FileLogger;
import backend.users.*;
import types.LogCategory;
import types.LogLevel;
import types.UserType;

public class PromoteUserCommand implements Command {

    private final BankSystem bankSystem;
    private final User executor;
    private final User target;
    private final UserType targetType; // νέο πεδίο για τον τύπο προαγωγής

    public PromoteUserCommand(BankSystem bankSystem, User executor, User target, UserType targetType) {
        this.bankSystem = bankSystem;
        this.executor = executor;
        this.target = target;
        this.targetType = targetType;
    }

    @Override
    public void execute() throws SecurityException {
        FileLogger logger = FileLogger.getInstance();

        if (!executor.canPromoteUser()) {
            logger.log(LogLevel.WARNING, LogCategory.USER, 
                       " UserId:" + executor.getUserID() + " attempted to promote UserId:" + target.getUserID() + " without sufficient permissions.");
            throw new SecurityException("You do not have permission to promote users.");
        }

        UserType current = target.getUserType();

        // Έλεγχος εγκυρότητας προαγωγής
        if (current == UserType.CUSTOMER) {
            if (targetType != UserType.EMPLOYEE && targetType != UserType.AUDITOR) {
                throw new SecurityException("Invalid promotion for CUSTOMER.");
            }
        } else if (current == UserType.EMPLOYEE) {
            if (targetType != UserType.ADMIN) {
                throw new SecurityException("Invalid promotion for EMPLOYEE.");
            }
        } else {
            throw new SecurityException("This user type cannot be promoted.");
        }

        // Αφαίρεση παλιού χρήστη
        bankSystem.removeUser(target.getUserID());

        UserBuilder builder = new UserBuilder()
                .withUsername(target.getUsername())
                .withPassword(target.getPassword())
                .withName(target.getName())
                .withSurname(target.getSurname())
                .withBranch(target.getBranch())
                .withAFM(target.getAFM());

        String UserID = bankSystem.generateId(targetType);
        User promoted = UserFactory.createUser(targetType, UserID, builder);
        bankSystem.addUser(promoted);

        logger.log(LogLevel.INFO, LogCategory.USER, 
                   " UserId:" + executor.getUserID() + " promoted to: " + targetType + " with new UserId:" + UserID);
    }

    @Override
    public void undo() {
        // TODO
    }
}
