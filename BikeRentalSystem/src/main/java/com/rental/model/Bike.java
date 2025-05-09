package com.rental.model;

import java.util.Objects;

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
        return "Bike{" +
                "bikeId='" + bikeId + '\'' +
                ", isAvailable=" + isAvailable +
                ", stationId=" + (currentStation != null ? currentStation.getStationId() : "none") +
                '}';
    }
}