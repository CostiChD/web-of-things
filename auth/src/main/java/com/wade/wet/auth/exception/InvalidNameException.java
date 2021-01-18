package com.wade.wet.auth.exception;

public class InvalidNameException extends Exception {
    public InvalidNameException() {
        super("Name field is invalid.");
    }
}
