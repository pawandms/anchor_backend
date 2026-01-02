package com.anchor.app.msg.exceptions;

import com.anchor.app.exceptions.BaseException;

/**
 * Custom exception for Channel Service operations
 */
public class ChannelServiceException extends BaseException {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * Constructor with message
     * 
     * @param message Error message
     */
    public ChannelServiceException(String message) {
        super(message);
    }
    
    /**
     * Constructor with message and throwable
     * 
     * @param message Error message
     * @param throwable Cause of the exception
     */
    public ChannelServiceException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
