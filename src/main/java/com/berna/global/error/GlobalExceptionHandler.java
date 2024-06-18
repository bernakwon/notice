package com.berna.global.error;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;


@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CommonException.class)
    public ErrorResponse handleException(CommonException e) {
        e.getStackTrace();
        return ErrorResponse.builder()
                .code(e.getErrorCode().getHttpStatus().value())
                .message(e.getErrorCode().getDetail()).build();
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ErrorResponse handleNoHandlerFound(CommonException e) {
        e.getStackTrace();
        return ErrorResponse.builder()
                .code(HttpStatus.NOT_FOUND.value())
                .message(HttpStatus.NOT_FOUND.getReasonPhrase()).build();
    }
}
