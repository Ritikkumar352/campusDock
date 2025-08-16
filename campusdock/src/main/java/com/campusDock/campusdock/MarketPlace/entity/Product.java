package com.campusDock.campusdock.MarketPlace.entity;

import com.campusDock.campusdock.entity.College;
import com.campusDock.campusdock.entity.MediaFile;
import com.campusDock.campusdock.entity.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private BigDecimal price;


    @Column(name = "listed_on", nullable = false)
    private LocalDateTime listedOn;

    private boolean isServie=true;

    // Many-to-one relationship with the User who listed the product
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false) // user_id is the foreign key column
    private User user;

    // Many-to-one relationship with the College the product belongs to
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "college_id", nullable = false) // college_id is the foreign key column
    private College college;

    // photos of the product to ssell or service
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<MediaFile> mediaFiles = new ArrayList<>();

}
