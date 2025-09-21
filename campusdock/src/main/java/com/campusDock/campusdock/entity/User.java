//package com.campusDock.campusdock.entity;
//
//import com.fasterxml.jackson.annotation.JsonBackReference;
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//
//import java.util.UUID;
//
//@Entity
//@Table(name = "users")
//
//public class User
//{
//    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
//    private UUID id;
//
//    @Column(nullable = false)
//    private String name;
//
//    @Column(nullable = false)
//    private String email;
//
//    @Column
//    private String password;
//
//    @Enumerated(EnumType.STRING)
//    @Column(nullable = false)
//    private Role role;
//
//    @ManyToOne
//    @JoinColumn(name = "college_id")
//    @JsonBackReference
//    private College college;
//
//    public User() {
//    }
//
//    public User(College college, String email, UUID id, String name, String password) {
//        this.college = college;
//        this.email = email;
//        this.id = id;
//        this.name = name;
//        this.password = password;
//    }
//
//    public String getPassword() {
//        return password;
//    }
//
//    public void setPassword(String password) {
//        this.password = password;
//    }
//
//    public College getCollege() {
//        return college;
//    }
//
//    public void setCollege(College college) {
//        this.college = college;
//    }
//
//    public String getEmail() {
//        return email;
//    }
//
//    public void setEmail(String email) {
//        this.email = email;
//    }
//
//    public UUID getId() {
//        return id;
//    }
//
//    public void setId(UUID id) {
//        this.id = id;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//}


package com.campusDock.campusdock.entity;

import com.campusDock.campusdock.MarketPlace.entity.Product;
import com.campusDock.campusdock.Socials.Entity.Post;
import com.campusDock.campusdock.Socials.Entity.XPLog;
import com.campusDock.campusdock.entity.Enum.UserRole;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class User {
    @Id
    @EqualsAndHashCode.Include   // uses only id in hash ... or whatever is markd to use
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String anonymousName;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

    // use fetch lazy ???
    @ManyToOne
    @JoinColumn(name = "college_id", referencedColumnName = "id")
    @JsonBackReference
    private College college;


    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private Cart cart;

    // 3. User - Canteen (Owner)
    @OneToOne(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private Canteen canteenOwner;



    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Post> posts;       //To access different post by single user

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<XPLog> xpLogs;    //For profile points

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private Set<Product> products = new HashSet<>();


}
