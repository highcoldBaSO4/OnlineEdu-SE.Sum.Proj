package com.se231.onlineedu.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author liu
 */
@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "该课程原型暂不可用")
public class CoursePrototypeUnavailableException extends RuntimeException{
}
