package com.campusDock.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "menu_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MenuItems {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false)
    private String foodName;

    @Column(nullable = false)
    private double price;

    private String description;

    private boolean isAvailable;

    private String timeToCook;

    // 1. MenuItems - Canteen
    @ManyToOne
    @JoinColumn(name = "canteen_id", referencedColumnName = "id")
    @JsonBackReference
    private Canteen canteen;

    // 2. MenuItems - Media
    @OneToMany(mappedBy = "menuItems", cascade = CascadeType.ALL,orphanRemoval = true)
    @JsonManagedReference
    private List<MediaFile> MediaFile;   // use mediaFiles
}
