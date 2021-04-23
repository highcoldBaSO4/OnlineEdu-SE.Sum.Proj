package com.se231.onlineedu.exception;

/**
 * @author liu
 */
public class BulkImportDataException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public BulkImportDataException(String message) {
        super(message);
    }
}
