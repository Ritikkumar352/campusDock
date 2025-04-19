package com.campusDock.campusdock.entity;

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
public class MenuItem {
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

    @ManyToOne
    @JoinColumn(name = "canteen_id", referencedColumnName = "id")
    @JsonBackReference
    private Canteen canteen;

    @OneToMany(mappedBy = "menuItem", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<MediaFile> MediaFile;
}
