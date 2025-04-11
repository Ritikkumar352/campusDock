package com.campusDock.campusdock.entity;

import jakarta.persistence.*;

import java.awt.*;
import java.util.UUID;


@Entity
public class Canteen {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;

    private String name;
    private boolean openStatus;

}

