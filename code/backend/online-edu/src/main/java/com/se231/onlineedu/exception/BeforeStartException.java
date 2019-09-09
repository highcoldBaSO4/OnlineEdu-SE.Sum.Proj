package com.se231.onlineedu.exception;
/**
 * @author liu
 * @date 2019/07/11
 */
public class BeforeStartException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public BeforeStartException(String message) {
        super(message);
    }
}
