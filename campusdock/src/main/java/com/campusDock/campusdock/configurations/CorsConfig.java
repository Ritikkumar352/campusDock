package com.campusDock.campusdock.configurations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();

        // Set your allowed origins
        config.setAllowedOrigins(List.of("http://localhost:5173", "http://localhost:3000")); // Vite, React, etc.

        // Allow credentials (cookies, auth headers, etc.)
        config.setAllowCredentials(true);

        // Allow common headers
        config.addAllowedHeader("*");

        // Allow HTTP methods
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        // Expose headers (optional)
        config.addExposedHeader("Authorization");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config); // Apply to all routes

        return new CorsFilter(source);
    }
}