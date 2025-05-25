package com.rental.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Reprezentuje użytkownika systemu wypożyczalni rowerów,
 * przechowuje jego identyfikator, imię, historię wypożyczeń
 * oraz informację o bieżącym wypożyczeniu.
 */
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

    /**
     * Sprawdza, czy użytkownik aktualnie wypożycza rower.
     *
     * @return {@code true} jeśli istnieje aktywne wypożyczenie
     */
    public boolean isRenting() {
        return currentRental != null;
    }

    /**
     * Rozpoczyna nowe wypożyczenie dla użytkownika.
     *
     * @param rental obiekt wypożyczenia do rozpoczęcia
     * @throws IllegalStateException jeśli użytkownik ma już
     *         aktywne wypożyczenie
     */
    public void startRental(Rental rental) {
        if (isRenting()) {
            throw new IllegalStateException("Użytkownik już wypożycza rower.");
        }
        this.currentRental = rental;
    }

    /**
     * Kończy bieżące wypożyczenie użytkownika, dodaje je do historii
     * i zeruje stan bieżącego wypożyczenia.
     *
     * @throws IllegalStateException jeśli użytkownik nie ma
     *         aktywnego wypożyczenia
     */
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
