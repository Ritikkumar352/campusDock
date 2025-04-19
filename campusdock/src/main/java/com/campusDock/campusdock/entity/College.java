//package com.campusDock.campusdock.entity;
//
//
//import com.fasterxml.jackson.annotation.JsonManagedReference;
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.UUID;
//
//@Entity
//@Table(name = "colleges")
//public class College
//{
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
//    private UUID id;
//
//    @Column(nullable = false)
//    private String name;
//
//    @Column(nullable = false)
//    private String domain;
//
//    //@OneToMany(mappedBy = "college")----tells JPA that this list of users is mapped by the college field in the User entity.
//    //cascade = CascadeType.ALL---- if you save or delete a college, related users will be affected too.
//    //fetch = FetchType.LAZY---- users are fetched only when needed (saves memory).
//    @OneToMany(mappedBy = "college",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
//    @JsonManagedReference
//    private List<User> collegeUsers=new ArrayList<>();
//
//    public College() {
//    }
//
//    public College(List<User> collegeUsers, String domain, UUID id, String name) {
//        this.collegeUsers = collegeUsers;
//        this.domain = domain;
//        this.id = id;
//        this.name = name;
//    }
//
//    public List<User> getCollegeUsers() {
//        return collegeUsers;
//    }
//
//    public void setCollegeUsers(List<User> collegeUsers) {
//        this.collegeUsers = collegeUsers;
//    }
//
//    public String getDomain() {
//        return domain;
//    }
//
//    public void setDomain(String domain) {
//        this.domain = domain;
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
//
//

package com.campusDock.campusdock.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "colleges")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class College {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String domain;

    @OneToMany(mappedBy = "college", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<User> users;

    @OneToMany(mappedBy = "college", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Canteen> canteens;
}

