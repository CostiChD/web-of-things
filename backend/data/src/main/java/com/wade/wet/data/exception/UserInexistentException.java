package com.wade.wet.data.exception;

public class UserInexistentException extends Exception {
    public UserInexistentException() {
        super("User doesn't exist");
    }
}
