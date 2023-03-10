package com.example.boxinator.errors.handlers;


import com.example.boxinator.errors.ApiErrorResponse;
import com.example.boxinator.errors.exceptions.ApplicationException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@Slf4j
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ApplicationExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<?> handleApplicationException(final ApplicationException exception, final HttpServletRequest request) {
        log.error(
                String.format("ApplicationExceptionHandler caught an ApplicationException: %s", exception.getMessage()),
                exception
        );
        var response = new ApiErrorResponse(
                exception.getMessage(),
                exception.getHttpStatus().value(),
                request.getRequestURI(),
                request.getMethod(),
                LocalDateTime.now().toString()
        );
        return new ResponseEntity<>(response, exception.getHttpStatus());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleUnknownException(final Exception exception, final HttpServletRequest request) {
        log.error(
                String.format("ApplicationExceptionHandler caught an UnknownException: %s", exception.getMessage()),
                exception
        );
        var response = new ApiErrorResponse(
                "Internal server error.",
                500,
                request.getRequestURI(),
                request.getMethod(),
                LocalDateTime.now().toString()
        );
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}