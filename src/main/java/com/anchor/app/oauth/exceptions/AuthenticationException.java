package com.anchor.app.oauth.exceptions;

/**
 * User: ${User}
 * Date: Aug, 2023
 * Comments:
 */
public class AuthenticationException extends org.springframework.security.core.AuthenticationException {
    public AuthenticationException(String msg, Throwable t) {
        super(msg, t);
    }

    public AuthenticationException(String msg) {
        super(msg);
    }
}
