package com.campusDock.campusdock.dto;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResendEmailRequest {
    private String from;
    private String to;
    private String subject;
    private String html;
}
