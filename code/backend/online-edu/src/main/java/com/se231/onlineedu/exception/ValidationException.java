package com.se231.onlineedu.exception;

/**
 * @author liu
 */
public class ValidationException extends RuntimeException{
    private static final long serialVersionUID = 1L;

    public ValidationException(String message) {
        super(message);
    }
}
