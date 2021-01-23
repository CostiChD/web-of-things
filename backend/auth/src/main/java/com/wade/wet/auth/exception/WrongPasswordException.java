package com.wade.wet.auth.exception;

public class WrongPasswordException extends Exception {
    public WrongPasswordException() {
        super("You entered the wrong password.");
    }
}
