package com.se231.onlineedu.exception;

/**
 * @author Zhe Li
 * @date 2019/07/19
 */
public class NotMatchException extends RuntimeException{
    private static final long serialVersionUID = 1L;

    public NotMatchException(String message){
        super(message);
    }
}
