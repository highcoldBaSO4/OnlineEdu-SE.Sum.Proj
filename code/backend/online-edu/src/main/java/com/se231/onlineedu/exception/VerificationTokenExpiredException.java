package com.se231.onlineedu.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author liu
 */
@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "验证码已过期")
public class VerificationTokenExpiredException extends RuntimeException {
}
