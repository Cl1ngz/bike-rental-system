package com.rental.exception;

public class StationNotFoundException extends Exception {
    public StationNotFoundException(String message) {
        super(message);
    }
}