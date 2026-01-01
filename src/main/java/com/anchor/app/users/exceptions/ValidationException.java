package com.anchor.app.users.exceptions;

import com.anchor.app.exceptions.BaseException;

import java.util.HashMap;
import java.util.Map;

public class ValidationException extends BaseException {
    
    private static final long serialVersionUID = 1L;
    
    private final Map<String, String> errors;
    
    public ValidationException(String message) {
        super(message);
        this.errors = new HashMap<>();
    }
    
    public ValidationException(String message, Throwable throwable) {
        super(message, throwable);
        this.errors = new HashMap<>();
    }
    
    public ValidationException(String message, Map<String, String> errors) {
        super(message);
        this.errors = errors;
    }
    
    public ValidationException(String message, Throwable throwable, Map<String, String> errors) {
        super(message, throwable);
        this.errors = errors;
    }
    
    public Map<String, String> getErrors() {
        return errors;
    }
    
    public void addError(String field, String message) {
        this.errors.put(field, message);
    }
    
    public boolean hasErrors() {
        return !errors.isEmpty();
    }
}
