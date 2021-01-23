package com.wade.wet.data.exception;

public class InvalidEmailException extends Exception {
    public InvalidEmailException() {
        super("The email is not valid.");
    }
}
