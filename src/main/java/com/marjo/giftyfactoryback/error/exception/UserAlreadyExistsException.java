package com.marjo.giftyfactoryback.error.exception;


public class UserAlreadyExistsException extends RuntimeException{

    public UserAlreadyExistsException(String message) {
        super(message);
    }

}
