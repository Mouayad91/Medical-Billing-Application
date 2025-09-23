package com.backend.app.exception;

/** Maps to HTTP 409 for business rule conflicts */
public class ConflictException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    
    public ConflictException() {
        super();
    }

    public ConflictException(String message) {
        super(message);
    }
}