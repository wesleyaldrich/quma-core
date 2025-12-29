package com.quma.app.common.exception;

import com.quma.app.common.constant.ErrorCode;
import com.quma.app.common.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /* Add custom exceptions handler here.
    *  Make sure to follow the convention below.
    *  */

    @ExceptionHandler(BadParameterException.class)
    public ResponseEntity<ErrorResponse> handleBadParameterException(BadParameterException e) {
        var response = ErrorResponse.builder()
                .errorCode(ErrorCode.BAD_REQUEST.getCode())
                .errorMessage(e.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    @ExceptionHandler(BadDeveloperException.class)
    public ResponseEntity<ErrorResponse> handleBadDeveloperException(BadDeveloperException e) {
        var response = ErrorResponse.builder()
                .errorCode(ErrorCode.BAD_CODE.getCode())
                .errorMessage(e.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleOtherExceptions(Exception e) {

        var response = ErrorResponse.builder()
                .errorCode(ErrorCode.UNEXPECTED_ERROR.getCode())
                .errorMessage(e.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(response);
    }
}
