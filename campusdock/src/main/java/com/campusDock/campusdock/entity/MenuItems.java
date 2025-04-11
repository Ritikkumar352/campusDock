package com.campusDock.campusdock.entity;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
public class MenuItems {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String foodName;
    private double price;
    private String description;
    private boolean availability;


}


