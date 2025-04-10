package com.rental.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class User {
    private String userId;
    private String name;
    private List<Rental> rentalHistory;
    private Rental currentRental;

    public User(String userId, String name) {
        this.userId = userId;
        this.name = name;
        this.rentalHistory = new ArrayList<>();
        this.currentRental = null; // null because no renting
    }

    public String getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public List<Rental> getRentalHistory() {
        return rentalHistory;
    }

    public Rental getCurrentRental() {
        return currentRental;
    }

    public boolean isRenting() {
        return currentRental != null;
    }

    public void startRental(Rental rental) {
        if (isRenting()) {
            throw new IllegalStateException("Użytkownik już wypożycza rower.");
        }
        this.currentRental = rental;
    }

    public void endRental() {
        if (!isRenting()) {
            throw new IllegalStateException("Użytkownik nie ma aktywnego wypożyczenia.");
        }
        this.rentalHistory.add(this.currentRental);
        this.currentRental = null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(userId, user.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId);
    }

    @Override
    public String toString() {
        return "User{" +
                "userId='" + userId + '\'' +
                ", name='" + name + '\'' +
                ", isRenting=" + isRenting() +
                '}';
    }
}
