package com.wade.wet.auth.exception;

public class InvalidEmailException extends Exception {
    public InvalidEmailException() {
        super("The email is not valid.");
    }
}
