package com.mdrsolutions.records_management;

public class PersonDoesNotExistException extends RuntimeException {
    public PersonDoesNotExistException() {
        super("Person does not exist");
    }

    public PersonDoesNotExistException(String message) {
        super(message);
    }
}
