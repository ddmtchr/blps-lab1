package com.ddmtchr.blpslab1.exception;

import org.springframework.security.core.AuthenticationException;

public class NoAuthenticationException extends AuthenticationException {
    public NoAuthenticationException(String message) {
        super(message);
    }
}
