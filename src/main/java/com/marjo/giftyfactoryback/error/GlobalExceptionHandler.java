package com.marjo.giftyfactoryback.error;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.marjo.giftyfactoryback.error.exception.NoResultException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NoResultException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    RestErrorResponse handleNoResultException(NoResultException ex) {
        return new RestErrorResponse( 
            HttpStatus.NOT_FOUND.value(), 
            ex.getMessage(), 
            LocalDateTime.now());
    }

    // Handle any other exception too.
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    RestErrorResponse handleException(Exception ex) {
        return new RestErrorResponse( 
            HttpStatus.BAD_REQUEST.value(), 
            ex.getMessage(), 
            LocalDateTime.now());
    }
}
