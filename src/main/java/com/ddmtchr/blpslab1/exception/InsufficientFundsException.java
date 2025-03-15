package com.ddmtchr.blpslab1.exception;

public class InsufficientFundsException extends RuntimeException {
    public InsufficientFundsException() {
        super();
    }

    public InsufficientFundsException(String msg) {
        super(msg);
    }
}
