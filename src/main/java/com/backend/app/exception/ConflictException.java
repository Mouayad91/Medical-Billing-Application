package com.backend.app.exception;

/**
 * Exception for conflict scenarios like overpayment or duplicate idempotency keys
 * Maps to HTTP 409 Conflict
 */
public class ConflictException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    
    public ConflictException() {
        super();
    }

    public ConflictException(String message) {
        super(message);
    }
}