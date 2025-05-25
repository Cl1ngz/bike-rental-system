package com.rental.service;

import com.rental.exception.*;
import com.rental.model.Bike;
import com.rental.model.Rental;
import com.rental.model.Station;
import com.rental.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Reprezentuje system wypożyczalni rowerów,
 * zarządza użytkownikami, stacjami, rowerami
 * oraz procesami wypożyczeń i zwrotów.
 */
public class BikeRentalSystem {
    private Map<String, User> users = new HashMap<>();
    private Map<String, Station> stations = new HashMap<>();
    private Map<String, Bike> bikes = new HashMap<>();

    // --- User Management ---

    /**
     * Rejestruje nowego użytkownika w systemie.
     *
     * @param userId unikalny identyfikator użytkownika
     * @param name   imię i nazwisko użytkownika
     * @return       utworzony obiekt User
     * @throws IllegalArgumentException jeśli użytkownik o podanym ID już istnieje
     */

    public User registerUser(String userId, String name) {
        if (users.containsKey(userId)) {
            throw new IllegalArgumentException("Użytkownik o ID " + userId + " już istnieje.");
        }
        User newUser = new User(userId, name);
        users.put(userId, newUser);
        System.out.println("Zarejestrowano użytkownika: " + newUser);
        return newUser;
    }

    /**
     * Wyszukuje użytkownika po identyfikatorze.
     *
     * @param userId identyfikator użytkownika
     * @return       obiekt User o podanym ID
     * @throws UserNotFoundException jeśli nie znaleziono użytkownika
     */
    public User findUser(String userId) throws UserNotFoundException {
        User user = users.get(userId);
        if (user == null) {
            throw new UserNotFoundException("Nie znaleziono użytkownika o ID: " + userId);
        }
        return user;
    }

    // --- Station Management ---

    /**
     * Dodaje nową stację do systemu.
     *
     * @param stationId    unikalny identyfikator stacji
     * @param locationName nazwa lub opis lokalizacji stacji
     * @param capacity     maksymalna liczba rowerów, które może pomieścić stacja
     * @return             utworzony obiekt Station
     * @throws IllegalArgumentException jeśli stacja o podanym ID już istnieje
     */
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

    /**
     * Tworzy nowy rower i umieszcza go na wskazanej stacji.
     *
     * @param bikeId           unikalny identyfikator roweru
     * @param initialStationId identyfikator stacji, na której rower ma się pojawić
     * @return                 utworzony obiekt Bike
     * @throws StationNotFoundException jeśli nie istnieje stacja o podanym ID
     * @throws StationFullException     jeśli stacja jest pełna
     * @throws IllegalArgumentException jeśli rower o podanym ID już istnieje
     */
    public Bike addBike(String bikeId, String initialStationId) throws StationNotFoundException, StationFullException {
        if (bikes.containsKey(bikeId)) {
            throw new IllegalArgumentException("Rower o ID " + bikeId + " już istnieje.");
        }
        Station station = findStation(initialStationId);
        if (station.isFull()) {
            throw new StationFullException("Nie można dodać roweru, stacja " + initialStationId + " jest pełna.");
        }

        Bike newBike = new Bike(bikeId);
        bikes.put(bikeId, newBike);
        station.dockBike(newBike); // dockBike ustawi też currentStation w rowerze
        System.out.println("Dodano rower: " + newBike + " do stacji " + station.getStationId());
        return newBike;
    }

    public Bike findBike(String bikeId) throws BikeNotFoundException {
        Bike bike = bikes.get(bikeId);
        if (bike == null) {
            throw new BikeNotFoundException("Nie znaleziono roweru o ID: " + bikeId);
        }
        return bike;
    }

    public List<Bike> getAvailableBikesAtStation(String stationId) throws StationNotFoundException {
        Station station = findStation(stationId);
        return station.getDockedBikes(); // Zwraca kopię listy rowerów na stacji
    }

    // --- Rental Management ---

    /**
     * Wypożycza rower użytkownikowi ze wskazanej stacji.
     *
     * @param userId    identyfikator użytkownika
     * @param stationId identyfikator stacji, z której pobierany jest rower
     * @return          utworzony obiekt Rental reprezentujący wypożyczenie
     * @throws UserNotFoundException        jeśli nie znaleziono użytkownika
     * @throws StationNotFoundException     jeśli nie znaleziono stacji
     * @throws UserAlreadyRentingException  jeśli użytkownik już ma aktywne wypożyczenie
     * @throws NoBikesAvailableException    jeśli na stacji brak dostępnych rowerów
     */
    public Rental rentBike(String userId,
                           String stationId) throws UserNotFoundException, StationNotFoundException, NoBikesAvailableException, UserAlreadyRentingException {
        User user = findUser(userId);
        Station station = findStation(stationId);

        if (user.isRenting()) {
            throw new UserAlreadyRentingException("Użytkownik " + userId + " już wypożycza rower.");
        }
        if (station.isEmpty()) {
            throw new NoBikesAvailableException("Brak dostępnych rowerów na stacji " + stationId);
        }

        // Take bike from station
        // Optional for safety
        Bike bikeToRent = station.undockBike()
                .orElseThrow(() -> new NoBikesAvailableException(
                        "Wystąpił nieoczekiwany błąd - brak rowerów mimo wcześniejszego sprawdzenia."));

        Rental newRental = new Rental(user, bikeToRent, station);

        // new state
        user.startRental(newRental);

        System.out.println(
                "Użytkownik " + userId + " wypożyczył rower " + bikeToRent.getBikeId() + " ze stacji " + stationId);
        return newRental;
    }

    /**
     * Zwraca rower na wskazaną stację i kończy wypożyczenie.
     *
     * @param bikeId    identyfikator zwracanego roweru
     * @param stationId identyfikator stacji, na którą zwracany jest rower
     * @return          obiekt Rental z zakończonym wypożyczeniem
     * @throws BikeNotFoundException       jeśli nie znaleziono roweru
     * @throws StationNotFoundException    jeśli nie znaleziono stacji
     * @throws StationFullException        jeśli stacja jest pełna
     * @throws NotRentingException         jeśli rower nie był wypożyczony
     */
    public Rental returnBike(String bikeId,
                             String stationId) throws BikeNotFoundException, StationNotFoundException, StationFullException, NotRentingException {

        Bike bike = findBike(bikeId); // check if exists

        // checks if really rented
        if (bike.isAvailable() || bike.getCurrentStation() != null) {
            throw new NotRentingException("Rower " + bikeId + " nie jest aktualnie wypożyczony.");
        }

        User rentingUser = findUserRentingBike(bikeId);

        // Ending station
        Station endStation = findStation(stationId);

        if (endStation.isFull()) {
            throw new StationFullException("Stacja " + stationId + " jest pełna. Nie można zwrócić roweru.");
        }

        // Zakończ wypożyczenie w obiekcie Rental
        Rental rentalToEnd = rentingUser.getCurrentRental();
        if (rentalToEnd == null || !rentalToEnd.getBike().equals(bike)) {
            throw new IllegalStateException("Niespójność danych - użytkownik nie wypożycza tego roweru.");
        }
        rentalToEnd.endRental(endStation);

        // update bike state (dock in new station)
        endStation.dockBike(bike);

        // update userstate (end rental)
        rentingUser.endRental();

        System.out.println(
                "Rower " + bikeId + " zwrócony na stację " + stationId + " przez użytkownika " + rentingUser.getUserId());
        System.out.println("Czas wypożyczenia: " + rentalToEnd.getDuration().toMinutes() + " minut.");

        return rentalToEnd;
    }

    // Find user renting a specific bike
    private User findUserRentingBike(String bikeId) throws NotRentingException {
        for (User user : users.values()) {
            if (user.isRenting() && user.getCurrentRental().getBike().getBikeId().equals(bikeId)) {
                return user;
            }
        }
        throw new NotRentingException("Nie znaleziono użytkownika aktualnie wypożyczającego rower o ID: " + bikeId);
    }

    // --- History ---
    public List<Rental> getUserHistory(String userId) throws UserNotFoundException {
        User user = findUser(userId);
        return user.getRentalHistory();
    }
}
