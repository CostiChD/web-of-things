package com.wade.wet.data.advice;


import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.OffsetDateTime;
import java.util.HashMap;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {IllegalArgumentException.class, IllegalStateException.class})
    protected ResponseEntity<Object> handleConflict(RuntimeException exception, WebRequest request) {
        HashMap<String, Object> fields = new HashMap<>();
        fields.put("timestamp", OffsetDateTime.now().toString());
        fields.put("message", exception.getMessage());
        return handleExceptionInternal(exception, fields, new HttpHeaders(), HttpStatus.CONFLICT, request);
    }

    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<Object> handleConflict(Exception exception, WebRequest request) {
        HashMap<String, Object> fields = new HashMap<>();
        fields.put("timestamp", OffsetDateTime.now().toString());
        fields.put("message", exception.getMessage());
        return handleExceptionInternal(exception, fields, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }
}