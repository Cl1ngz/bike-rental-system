package com.rental.exception;

public class NoBikesAvailableException extends Exception {
    public NoBikesAvailableException(String message) {
        super(message);
    }
}
