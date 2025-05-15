package com.rental.service;

import com.rental.exception.StationNotFoundException;
import com.rental.exception.UserNotFoundException;
import com.rental.model.Bike;
import com.rental.model.Station;
import com.rental.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BikeRentalSystem {
    private Map<String, User> users = new HashMap<>();
    private Map<String, Station> stations = new HashMap<>();
    private Map<String, Bike> bikes = new HashMap<>();

    // --- User Management ---
    public User registerUser(String userId, String name) {
        if (users.containsKey(userId)) {
            throw new IllegalArgumentException("Użytkownik o ID " + userId + " już istnieje.");
        }
        User newUser = new User(userId, name);
        users.put(userId, newUser);
        System.out.println("Zarejestrowano użytkownika: " + newUser);
        return newUser;
    }

    public User findUser(String userId) throws UserNotFoundException {
        User user = users.get(userId);
        if (user == null) {
            throw new UserNotFoundException("Nie znaleziono użytkownika o ID: " + userId);
        }
        return user;
    }

    // --- Station Management ---
    public Station addStation(String stationId, String locationName, int capacity) {
        if (stations.containsKey(stationId)) {
            throw new IllegalArgumentException("Stacja o ID " + stationId + " już istnieje.");
        }
        Station newStation = new Station(stationId, locationName, capacity);
        stations.put(stationId, newStation);
        System.out.println("Dodano stację: " + newStation);
        return newStation;
    }

    public Station findStation(String stationId) throws StationNotFoundException {
        Station station = stations.get(stationId);
        if (station == null) {
            throw new StationNotFoundException("Nie znaleziono stacji o ID: " + stationId);
        }
        return station;
    }

    public List<Station> getAllStations() {
        return new ArrayList<>(stations.values());
    }

    // --- Bike Management ---
    // TODO: Implement methods for managing bikes

    // --- Rental Management ---
    // TODO: Implement methods for managing rentals
}
