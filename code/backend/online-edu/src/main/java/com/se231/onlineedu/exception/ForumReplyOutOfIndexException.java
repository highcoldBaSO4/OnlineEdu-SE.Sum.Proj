package com.se231.onlineedu.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author liu
 */
@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "论坛回复帖子的索引错误")
public class ForumReplyOutOfIndexException extends RuntimeException {
}
