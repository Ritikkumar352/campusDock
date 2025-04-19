package com.campusDock.campusdock.entity.DTO;

import com.campusDock.campusdock.entity.College;
import com.campusDock.campusdock.entity.User;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class CollegeResponseDto {
    private UUID id;
    private String name;
    private String domain;
    private List<String> studentNames;

    public CollegeResponseDto(College college) {
        this.id = college.getId();
        this.name = college.getName();
        this.domain = college.getDomain();
        this.studentNames = college.getUsers()
                .stream()
                .map(User::getName)
                .collect(Collectors.toList());
    }

    // Getters & Setters

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getStudentNames() {
        return studentNames;
    }

    public void setStudentNames(List<String> studentNames) {
        this.studentNames = studentNames;
    }
}