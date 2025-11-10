package io.github.fintrack._common.exception;

import io.github.fintrack._common.dto.ErrorDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationExceptions(
            MethodArgumentNotValidException ex, WebRequest request) {

        log.info("Validation Failed: {}", ex.getMessage(), ex);

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));

        ErrorDetails errorDetails = new ErrorDetails(
                LocalDateTime.now(),
                "Validation Failed",
                errors.toString(),
                request.getDescription(false)
        );
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorDetails> handleResourceNotFoundException(
            ResourceNotFoundException ex, WebRequest request) {

        log.info("Resource Not Found: {}", ex.getMessage(), ex);

        ErrorDetails errorDetails = new ErrorDetails(
                LocalDateTime.now(),
                "Resource Not Found",
                ex.getMessage(),
                request.getDescription(false)
        );
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorDetails> handleHttpRequestMethodNotSupportedException(
            HttpRequestMethodNotSupportedException ex, WebRequest request) {

        log.info("Method invalid: {}", ex.getMessage(), ex);

        ErrorDetails errorDetails = new ErrorDetails(
                LocalDateTime.now(),
                "Method invalid",
                ex.getMessage(),
                request.getDescription(false)
        );
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDetails> handleGlobalException(
            Exception ex, WebRequest request) {

         log.error("Internal Server Error: {}", ex.getMessage(), ex);

        ErrorDetails errorDetails = new ErrorDetails(
                LocalDateTime.now(),
                "Internal Server Error",
                "An unexpected error occurred. Please try again later.",
                request.getDescription(false)
        );
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}