package com.wade.wet.auth.exception;

public class UserInexistentException extends Exception {
    public UserInexistentException() {
        super("User doesn't exist");
    }
}
