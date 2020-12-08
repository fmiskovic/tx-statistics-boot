package org.tx.statistics.errors;

public class TimestampError extends Exception {

    public TimestampError() {
        super("Invalid time stamp format.");
    }

    public TimestampError(String message) {
        super(message);
    }

    public TimestampError(String message, Throwable cause) {
        super(message, cause);
    }
}
