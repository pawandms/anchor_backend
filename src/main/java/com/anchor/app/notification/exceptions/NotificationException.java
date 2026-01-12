package com.anchor.app.notification.exceptions;

import com.anchor.app.exceptions.BaseException;

/**
 * Custom exception for Channel Service operations
 */
public class NotificationException extends BaseException {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * Constructor with message
     * 
     * @param message Error message
     */
    public NotificationException(String message) {
        super(message);
    }
    
    /**
     * Constructor with message and throwable
     * 
     * @param message Error message
     * @param throwable Cause of the exception
     */
    public NotificationException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
