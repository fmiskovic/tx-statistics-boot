package org.tx.statistics.errors;

public class EntityNotFoundError extends Exception {

    public EntityNotFoundError() {
        super("Unable to find entity with specified ID.");
    }

    public EntityNotFoundError(String message) {
        super(message);
    }
}
