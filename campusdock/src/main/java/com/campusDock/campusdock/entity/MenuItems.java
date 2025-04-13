package com.campusDock.campusdock.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MenuItems {
    @Id
    @GeneratedValue
    private UUID id;

    private String foodName;
    private double price;
    private String description;
    private boolean is_available;
    private String time_to_cook; // approx

    @ManyToOne
    @JoinColumn(name = "canteen_id")
    private Canteen canteen;

    @OneToMany(mappedBy = "menuItems",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MediaFiles> mediaFiles;


}


