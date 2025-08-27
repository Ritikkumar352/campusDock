package com.campusDock.campusdock.entity.Enum;

public enum UserRole {
    // Access Level
    STUDENT,     //  List of Canteens, College Social , College Market Place
    FACULTY,    //  List of Canteen, College Faculty Social *** , College Market Place ??
    CANTEEN_OWNER, // Just there Canteen edit and other canteens view only
    ADMIN,         // Moderate user posts register canteens, users
    SUPER_ADMIN     // EVERYTHING
}
