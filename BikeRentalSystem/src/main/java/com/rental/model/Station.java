package com.rental.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class Station {
    private String stationId;
    private String locationName;
    private int capacity;
    private List<Bike> dockedBikes;

    public Station(String stationId, String locationName, int capacity) {
        this.stationId = stationId;
        this.locationName = locationName;
        this.capacity = capacity;
        this.dockedBikes = new ArrayList<>();
    }

    public String getStationId() {
        return stationId;
    }

    public String getLocationName() {
        return locationName;
    }

    public int getCapacity() {
        return capacity;
    }

    public List<Bike> getDockedBikes() {
        return new ArrayList<>(dockedBikes);
    }

    public int getAvailableBikeCount() {
        return dockedBikes.size();
    }

    public int getAvailableSpots() {
        return capacity - dockedBikes.size();
    }

    // Metody zarządzania rowerami
    public boolean isFull() {
        return dockedBikes.size() >= capacity;
    }

    public boolean isEmpty() {
        return dockedBikes.isEmpty();
    }

    /**
     * Dodaje rower do stacji.
     *
     * @param bike Rower do dodania.
     * @throws IllegalStateException jeśli stacja jest pełna lub rower jest już na innej stacji.
     */
    public void dockBike(Bike bike) {
        if (isFull()) {
            throw new IllegalStateException("Stacja " + stationId + " jest pełna.");
        }
        if (bike.getCurrentStation() != null && !bike.getCurrentStation().equals(this)) {
            throw new IllegalStateException("Rower " + bike.getBikeId() + " jest zadokowany na innej stacji.");
        }
        if (!dockedBikes.contains(bike)) {
            dockedBikes.add(bike);
            bike.setCurrentStation(this);
            bike.setAvailable(true);
        }
    }

    /**
     * Usuwa dostępny rower ze stacji (np. w celu wypożyczenia).
     *
     * @return Optional z usuniętym rowerem lub pusty Optional, jeśli stacja jest pusta.
     */
    public Optional<Bike> undockBike() {
        if (isEmpty()) {
            return Optional.empty();
        }
        // first for the list bike
        Bike bikeToRemove = dockedBikes.remove(0);
        bikeToRemove.setCurrentStation(null);
        bikeToRemove.setAvailable(false); // marking as rented
        return Optional.of(bikeToRemove);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Station station = (Station) o;
        return Objects.equals(stationId, station.stationId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(stationId);
    }

    @Override
    public String toString() {
        return "Station{" +
                "stationId='" + stationId + '\'' +
                ", locationName='" + locationName + '\'' +
                ", availableBikes=" + getAvailableBikeCount() +
                ", capacity=" + capacity +
                '}';
    }
}