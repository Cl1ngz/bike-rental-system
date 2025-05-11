package com.rental.exception;

public class UserAlreadyRentingException extends  Exception {
    public UserAlreadyRentingException(String message) {
        super(message);
    }
}