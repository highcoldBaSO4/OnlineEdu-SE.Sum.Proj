package com.se231.onlineedu.exception;

/**
 * @author liu
 */
public class EmptyFileException extends RuntimeException{
    private static final long serialVersionUID = 1L;

    public EmptyFileException(String message) {
        super(message);
    }
}
