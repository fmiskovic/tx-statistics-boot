package org.tx.statistics.errors;

public class EntityExistsError extends Exception {

    public EntityExistsError() {
        super("Entity with the given ID already exist.");
    }

    public EntityExistsError(String message) {
        super(message);
    }
}
