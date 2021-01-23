package com.wade.wet.data.exception;

public class WrongPasswordException extends Exception {
    public WrongPasswordException() {
        super("You entered the wrong password.");
    }
}
