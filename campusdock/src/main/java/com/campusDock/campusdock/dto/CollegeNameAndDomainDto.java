package com.campusDock.campusdock.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CollegeNameAndDomainDto {
    private String name;
    private String domain;
}
