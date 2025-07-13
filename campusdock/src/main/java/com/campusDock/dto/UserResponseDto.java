//package com.campusDock.dto;
//
//import lombok.Builder;
//import lombok.Data;
//
//import java.util.UUID;
//
//@Data
//@Builder
//public class UserResponseDto {
//    private UUID id;
//    private String email;
//    private String name;
//    private String collegeName;
//
//    public UserResponseDto() {
//    }
//
//    public UserResponseDto(String collegeName, String email, UUID id, String name) {
//        this.collegeName = collegeName;
//        this.email = email;
//        this.id = id;
//        this.name = name;
//    }
//
//    public String getCollegeName() {
//        return collegeName;
//    }
//
//    public void setCollegeName(String collegeName) {
//        this.collegeName = collegeName;
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