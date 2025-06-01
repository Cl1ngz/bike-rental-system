package com.rental.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Reprezentuje stację rowerową o określonym identyfikatorze,
 * nazwie lokalizacji oraz pojemności. Umożliwia zadokowanie
 * i wypożyczenie rowerów.
 */
public class Station {
    private String stationId;
    private String locationName;
    private int capacity;
    private List<Bike> dockedBikes;

    public Station(String stationId, String locationName, int capacity) {
        this.stationId = stationId.toUpperCase();
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

    /**
     * Sprawdza, czy stacja jest pełna (brak wolnych miejsc).
     *
     * @return {@code true} jeśli liczba rowerów ≥ pojemność
     */
    public boolean isFull() {
        return dockedBikes.size() >= capacity;
    }

    public boolean isEmpty() {
        return dockedBikes.isEmpty();
    }

    /**
     * Dodaje rower do stacji.
     *
     * @param bike obiekt roweru do zadokowania
     * @throws IllegalStateException jeśli stacja jest pełna
     *                               lub rower jest już zadokowany w innej stacji
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
     * Usuwa pierwszy dostępny rower ze stacji i zwraca go.
     *
     * @return {@code Optional} z usuniętym rowerem lub pusty
     * jeśli na stacji nie ma rowerów
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