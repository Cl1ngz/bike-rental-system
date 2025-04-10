package com.rental.model;
import java.time.Duration;
import java.time.LocalDateTime;

public class Rental {
    private String rentalId;
    private User user;
    private Bike bike;
    private Station startStation;
    private Station endStation; // null do momentu zwrotu
    private LocalDateTime startTime;
    private LocalDateTime endTime; // null do momentu zwrotu
    private Duration duration; // null do momentu zwrotu

}
