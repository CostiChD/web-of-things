package com.wade.wet.data.exception;

public class InvalidNameException extends Exception {
    public InvalidNameException() {
        super("Name field is invalid.");
    }
}
