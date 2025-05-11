package com.rental.exception;

public class BikeNotFoundException extends Exception {
    public BikeNotFoundException(String message) {
        super(message);
    }
}