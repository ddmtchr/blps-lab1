package com.ddmtchr.blpslab1.exception;

public class NoPermissionException extends RuntimeException {
    public NoPermissionException() {
        super();
    }

    public NoPermissionException(String msg) {
        super(msg);
    }
}
