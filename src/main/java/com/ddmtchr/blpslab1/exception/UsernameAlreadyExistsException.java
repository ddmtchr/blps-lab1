package com.ddmtchr.blpslab1.exception;

public class UsernameAlreadyExistsException extends RuntimeException {
    public UsernameAlreadyExistsException() {
        super();
    }

    public UsernameAlreadyExistsException(String msg) {
        super(msg);
    }
}
