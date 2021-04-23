package com.se231.onlineedu.exception;

/**
 * @author liu
 */
public class EndBeforeStartException extends RuntimeException{
    private static final long serialVersionUID = 1L;

    public EndBeforeStartException(String message) {
        super(message);
    }
}
