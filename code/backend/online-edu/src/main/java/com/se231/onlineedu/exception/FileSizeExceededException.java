package com.se231.onlineedu.exception;

/**
 * @author liu
 */
public class FileSizeExceededException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public FileSizeExceededException(String message) {
        super(message);
    }
}
