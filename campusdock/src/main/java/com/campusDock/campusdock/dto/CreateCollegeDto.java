package com.campusDock.campusdock.dto;

import lombok.Data;
import lombok.Getter;


public class CreateCollegeDto
{
    private String name;
    private String domain;

    public String getDomain() {
        return domain;
    }

    public String getName() {
        return name;
    }
}
