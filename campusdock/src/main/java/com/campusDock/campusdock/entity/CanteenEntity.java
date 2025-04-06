package com.campusDock.campusdock.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.util.UUID;

@Entity
public class CanteenEntity {

    @Id
    private UUID id;

}