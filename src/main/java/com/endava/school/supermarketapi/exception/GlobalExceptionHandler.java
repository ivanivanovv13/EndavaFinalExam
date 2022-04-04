package com.endava.school.supermarketapi.exception;

import com.endava.school.supermarketapi.model.Supermarket;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.*;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ItemNotFoundException.class)
    public ResponseEntity<ErrorResponse> countryDoesNotExist(ItemNotFoundException exception) {
        ErrorResponse errors = new ErrorResponse();
        errors.setTimeStamp(LocalDateTime.now());
        errors.setMessage(exception.getMessage());
        errors.setStatus(HttpStatus.NOT_FOUND.value());

        return new ResponseEntity<>(errors, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(SupermarketNotFoundException.class)
    public ResponseEntity<ErrorResponse> countryDoesNotExist(SupermarketNotFoundException exception) {
        ErrorResponse errors = new ErrorResponse();
        errors.setTimeStamp(LocalDateTime.now());
        errors.setMessage(exception.getMessage());
        errors.setStatus(HttpStatus.NOT_FOUND.value());

        return new ResponseEntity<>(errors, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MoneyAreNotEnoughException.class)
    public ResponseEntity<ErrorResponse> countryDoesNotExist(MoneyAreNotEnoughException exception) {
        ErrorResponse errors = new ErrorResponse();
        errors.setTimeStamp(LocalDateTime.now());
        errors.setMessage(exception.getMessage());
        errors.setStatus(HttpStatus.NOT_FOUND.value());

        return new ResponseEntity<>(errors, HttpStatus.NOT_FOUND);
    }
}
