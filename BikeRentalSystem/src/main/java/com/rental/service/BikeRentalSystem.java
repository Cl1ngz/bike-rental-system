package com.rental.service;

import com.rental.exception.*;
import com.rental.model.Bike;
import com.rental.model.Rental;
import com.rental.model.Station;
import com.rental.model.User;

import java.util.List;

public class BikeRentalSystem {
    // --- Rental Management ---
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

        // Pobierz rower ze stacji
        // Optional jest tu użyty dla bezpieczeństwa, chociaż sprawdziliśmy isEmpty()
        Bike bikeToRent = station.undockBike()
                .orElseThrow(() -> new NoBikesAvailableException(
                        "Wystąpił nieoczekiwany błąd - brak rowerów mimo wcześniejszego sprawdzenia."));

        // Utwórz nowy obiekt Rental
        Rental newRental = new Rental(user, bikeToRent, station);

        // Zaktualizuj stan użytkownika
        user.startRental(newRental);

        System.out.println(
                "Użytkownik " + userId + " wypożyczył rower " + bikeToRent.getBikeId() + " ze stacji " + stationId);
        return newRental;
    }

    public Rental returnBike(String bikeId,
                             String stationId) throws BikeNotFoundException, StationNotFoundException, StationFullException, NotRentingException {

        Bike bike = findBike(bikeId); // check if exists

        // checks if really rented
        if (bike.isAvailable() || bike.getCurrentStation() != null) {
            throw new NotRentingException("Rower " + bikeId + " nie jest aktualnie wypożyczony.");
        }

        // Znajdź użytkownika, który wypożycza ten rower (przez Rental)
        // Potrzebujemy sposobu na znalezienie aktywnego wypożyczenia dla danego roweru
        // Można przeszukać wszystkich użytkowników lub trzymać mapę aktywnych wypożyczeń
        User rentingUser = findUserRentingBike(bikeId); // Implementacja tej metody poniżej

        // Znajdź stację docelową
        Station endStation = findStation(stationId);

        // Sprawdź, czy stacja nie jest pełna (opcjonalne)
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

    // Metoda pomocnicza do znalezienia użytkownika wypożyczającego dany rower
    private User findUserRentingBike(String bikeId) throws NotRentingException {
        for (User user : users.values()) {
            if (user.isRenting() && user.getCurrentRental().getBike().getBikeId().equals(bikeId)) {
                return user;
            }
        }
        throw new NotRentingException("Nie znaleziono użytkownika aktualnie wypożyczającego rower o ID: " + bikeId);
    }

    // --- Pobieranie Historii ---
    public List<Rental> getUserHistory(String userId) throws UserNotFoundException {
        User user = findUser(userId);
        return user.getRentalHistory();
    }
}
