package com.rental.model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

public class Rental {
    private String rentalId;
    private User user;
    private Bike bike;
    private Station startStation;
    private Station endStation;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Duration duration;


    public Rental(User user, Bike bike, Station startStation) {
        this.rentalId = UUID.randomUUID().toString(); // simplified unique ID
        this.user = user;
        this.bike = bike;
        this.startStation = startStation;
        this.startTime = LocalDateTime.now();

        // null until return/endRetail
        this.endStation = null;
        this.endTime = null;
        this.duration = null;
    }

    public void endRental(Station endStation) {
        // TODO: method for ending rental
    }

    public String getRentalId() {
        return rentalId;
    }

    public User getUser() {
        return user;
    }

    public Bike getBike() {
        return bike;
    }

    public Station getStartStation() {
        return startStation;
    }

    public Station getEndStation() {
        return endStation;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public Duration getDuration() {
        return duration;
    }

    // TODO: toString()

}
