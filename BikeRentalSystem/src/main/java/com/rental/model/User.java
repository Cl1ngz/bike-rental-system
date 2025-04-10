package com.rental.model;

import java.util.List;

public class User {
    private String userId;
    private String name;
    private List<Rental> rentalHistory;
    private Rental currentRental; // Może być null jeśli nic nie wypożycza

    

}
