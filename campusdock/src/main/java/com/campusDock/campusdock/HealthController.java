package com.campusDock.campusdock;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController
{
    @GetMapping
    public String HealthCheck()
    {
        return "Campus Dock first prototype \n Everything's working";
    }
}
