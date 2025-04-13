package com.campusDock.campusdock.entity;


import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue
    private Long id;

    private LocalDateTime timestamp;
    private String status;
    private String paymentMode;
    private String extraNote;

}

