package com.rental.model;

import java.util.Objects;

/**
 * Reprezentuje pojedynczy rower w systemie, identyfikowany
 * unikalnym identyfikatorem, z informacją o dostępności
 * oraz o aktualnej stacji (lub braku stacji, gdy jest wypożyczony).
 */
public class Bike {
    private String bikeId;
    private boolean isAvailable;
    private Station currentStation; // null if rented

    public Bike(String bikeId) {
        this.bikeId = bikeId;
        this.isAvailable = true;
        this.currentStation = null;
    }

    public String getBikeId() {
        return bikeId;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public Station getCurrentStation() {
        return currentStation;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public void setCurrentStation(Station station) {
        this.currentStation = station;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bike bike = (Bike) o;
        return Objects.equals(bikeId, bike.bikeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bikeId);
    }

    @Override
    public String toString() {
        return "Rower{" +
                "bikeId='" + bikeId + '\'' + " Status = " +
                (isAvailable ? "Dostępny" : "Niedostępny") +
                ", Id stacji=" + (currentStation != null ? currentStation.getStationId() : "none") +
                '}';
    }
}