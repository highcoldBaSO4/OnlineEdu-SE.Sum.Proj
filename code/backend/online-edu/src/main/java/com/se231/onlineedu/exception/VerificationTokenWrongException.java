package com.se231.onlineedu.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author liu
 */
@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "验证码错误")
public class VerificationTokenWrongException extends RuntimeException {
}
