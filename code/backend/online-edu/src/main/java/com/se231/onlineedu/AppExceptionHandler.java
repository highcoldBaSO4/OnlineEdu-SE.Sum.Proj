package com.se231.onlineedu;

import com.se231.onlineedu.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;

/**
 * @author liu
 * @date 2019/07/11
 */
@RestControllerAdvice
public class AppExceptionHandler {
    @ResponseBody
    @ExceptionHandler(value = ValidationException.class)
    public ResponseEntity<String> handleException(ValidationException exception){
        return ResponseEntity.badRequest().body(exception.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(value = NotFoundException.class)
    public ResponseEntity<String> handleException(NotFoundException exception){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(value = AfterEndException.class)
    public ResponseEntity<String> handlerException(AfterEndException exception){
        return ResponseEntity.badRequest().body(exception.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(value = BeforeStartException.class)
    public ResponseEntity<String> handlerException(BeforeStartException exception){
        return ResponseEntity.badRequest().body(exception.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(value = FileFormatNotSupportException.class)
    public ResponseEntity<String> handlerException(FileFormatNotSupportException exception){
        return ResponseEntity.badRequest().body(exception.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(value = FileSizeExceededException.class)
    public ResponseEntity<String> handlerException(FileSizeExceededException exception){
        return ResponseEntity.badRequest().body(exception.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(value = IOException.class)
    public ResponseEntity<String> handlerException(IOException exception){
        return ResponseEntity.status(500).body(exception.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(value = EmptyFileException.class)
    public ResponseEntity<String> handlerException(EmptyFileException exception){
        return ResponseEntity.badRequest().body(exception.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(value = BulkImportDataException.class)
    public ResponseEntity<String> handlerException(BulkImportDataException exception){
        return ResponseEntity.badRequest().body(exception.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(value = IdentityException.class)
    public ResponseEntity<String> handlerException(IdentityException exception){
        return ResponseEntity.badRequest().body(exception.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(value = EndBeforeStartException.class)
    public ResponseEntity<String> handlerException(EndBeforeStartException exception){
        return ResponseEntity.badRequest().body(exception.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(value = AnswerException.class)
    public ResponseEntity<String> handlerException(AnswerException exception){
        return ResponseEntity.badRequest().body(exception.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(value = NotMatchException.class)
    public ResponseEntity<String> handlerException(NotMatchException exception){
        return ResponseEntity.badRequest().body(exception.getMessage());
    }

}

