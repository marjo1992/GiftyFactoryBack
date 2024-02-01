package com.marjo.giftyfactoryback.error.exception;

public class NotAuthorizedActionException extends RuntimeException {

    public NotAuthorizedActionException(String message) {
        super(message);
    }
}
