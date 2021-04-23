package com.se231.onlineedu.exception;

/**
 * @author liu
 */
public class FileFormatNotSupportException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public FileFormatNotSupportException(String message) {
        super(message);
    }
}
