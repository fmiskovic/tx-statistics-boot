package org.tx.statistics.errors;

public class InvalidAmountError extends Exception {

    public InvalidAmountError() {
        super("Invalid amount format.");
    }

    public InvalidAmountError(String message) {
        super(message);
    }

    public InvalidAmountError(String message, Throwable cause) {
        super(message, cause);
    }
}
