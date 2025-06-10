
package com.rental.ui;

import com.rental.exception.*;
import com.rental.model.Bike;
import com.rental.model.Rental;
import com.rental.model.Station;
import com.rental.model.User;
import com.rental.service.BikeRentalSystem;

import java.util.List;
import java.util.Scanner;

public class Main {
    private static final BikeRentalSystem system = new BikeRentalSystem();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        initializeSystem();

        boolean exit = false;
        while (!exit) {
            printMenu();
            int choice = getUserChoice();

            try {
                switch (choice) {
                    case 1 -> registerUser();
                    case 2 -> viewStations();
                    case 3 -> viewAvailableBikes();
                    case 4 -> rentBike();
                    case 5 -> returnBike();
                    case 6 -> viewUserHistory();
                    case 0 -> exit = true;
                    default -> System.out.println("Nieprawidłowy wybór.");
                }
            } catch (Exception e) {
                System.err.println("Błąd: " + e.getMessage());
            }
            System.out.println("---");
        }
        System.out.println("Do widzenia!");
        scanner.close();
    }

    private static void initializeSystem() {
        try {
            system.addStation("S1", "Centrum", 10);
            system.addStation("S2", "Politechnika", 8);
            system.addStation("S3", "Rynek", 12);

            system.addBike("B001", "S1");
            system.addBike("B002", "S1");
            system.addBike("B003", "S1");
            system.addBike("B004", "S2");
            system.addBike("B005", "S2");
            system.addBike("B006", "S3");

            system.registerUser("U1", "Jan Kowalski");
            system.registerUser("U2", "Anna Nowak");
        } catch (Exception e) {
            System.err.println("Błąd inicjalizacji systemu: " + e.getMessage());
        }
        System.out.println("\nSystem zainicjalizowany.\n");
    }

    private static void printMenu() {
        System.out.println("===== MENU WYPOŻYCZALNI ROWERÓW =====");
        System.out.println("1. Zarejestruj nowego użytkownika");
        System.out.println("2. Wyświetl stacje");
        System.out.println("3. Wyświetl dostępne rowery na stacji");
        System.out.println("4. Wypożycz rower");
        System.out.println("5. Zwróć rower");
        System.out.println("6. Wyświetl historię wypożyczeń użytkownika");
        System.out.println("0. Wyjdź");
        System.out.print("Wybierz opcję: ");
    }

    private static int getUserChoice() {
        while (!scanner.hasNextInt()) {
            System.out.println("Proszę podać liczbę.");
            scanner.next();
            System.out.print("Wybierz opcję: ");
        }
        int choice = scanner.nextInt();
        scanner.nextLine();
        return choice;
    }

    private static void registerUser() {
        System.out.println(
                "INFORMACJA: Id działa na zasadzie pseudonimu którego będziesz używać do wypożyczenia roweru.");
        System.out.print("Podaj ID użytkownika: ");
        String userId = scanner.nextLine();
        System.out.print("Podaj imię i nazwisko: ");
        String name = scanner.nextLine();
        try {
            system.registerUser(userId, name);
        } catch (IllegalArgumentException e) {
            System.err.println("Błąd rejestracji: " + e.getMessage());
        }
    }

    private static void viewStations() {
        System.out.println("\n--- Lista Stacji ---");
        List<Station> stations = system.getAllStations();
        if (stations.isEmpty()) {
            System.out.println("Brak stacji w systemie.");
        } else {
            stations.forEach(station ->
                    System.out.printf("ID: %s, Nazwa: %s, Dostępne rowery: %d, Wolne miejsca: %d / %d%n",
                            station.getStationId(), station.getLocationName(),
                            station.getAvailableBikeCount(), station.getAvailableSpots(), station.getCapacity())
            );
        }
    }

    private static void viewAvailableBikes() throws StationNotFoundException {
        System.out.print("Podaj ID stacji: ");
        String stationId = scanner.nextLine();
        List<Bike> bikes = system.getAvailableBikesAtStation(stationId.toUpperCase());
        System.out.println("\n--- Dostępne rowery na stacji " + stationId + " ---");
        if (bikes.isEmpty()) {
            System.out.println("Brak dostępnych rowerów.");
        } else {
            bikes.forEach(System.out::println);
        }
    }

    private static void rentBike() throws UserNotFoundException, StationNotFoundException, NoBikesAvailableException, UserAlreadyRentingException {
        System.out.print("Podaj swoje ID użytkownika: ");
        String userId = scanner.nextLine();
        System.out.print("Podaj ID stacji, z której chcesz wypożyczyć rower: ");
        String stationId = scanner.nextLine();
        var temp = system.rentBike(userId, stationId.toUpperCase());
        System.out.println("Wypożyczono rower(" + temp.getBike().getBikeId() + ")! ");
    }

    private static void returnBike() throws BikeNotFoundException, StationNotFoundException, StationFullException, NotRentingException {
        System.out.print("Podaj ID roweru, który zwracasz: ");
        String bikeId = scanner.nextLine();
        System.out.print("Podaj ID stacji, na którą zwracasz rower: ");
        String stationId = scanner.nextLine();
        Rental completedRental = system.returnBike(bikeId, stationId.toUpperCase());
        System.out.println("Rower zwrócony pomyślnie.");
        System.out.println("Podsumowanie wypożyczenia: " + completedRental);
    }

    private static void viewUserHistory() throws UserNotFoundException {
        System.out.print("Podaj ID użytkownika, którego historię chcesz zobaczyć: ");
        String userId = scanner.nextLine();
        List<Rental> history = system.getUserHistory(userId);
        System.out.println("\n--- Historia wypożyczeń użytkownika " + userId + " ---");
        if (history.isEmpty()) {
            System.out.println("Brak historii wypożyczeń.");
        } else {
            history.forEach(System.out::println);
        }

        User user = system.findUser(userId);
        if (user.isRenting()) {
            System.out.println("Aktywne wypożyczenie: " + user.getCurrentRental());
        }
    }
}
