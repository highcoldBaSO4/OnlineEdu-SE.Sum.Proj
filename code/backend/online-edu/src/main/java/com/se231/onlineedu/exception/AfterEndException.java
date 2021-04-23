package com.se231.onlineedu.exception;

/**
 * @author liu
 * @date 2019/07/11
 */
public class AfterEndException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public AfterEndException(String message) {
        super(message);
    }
}
