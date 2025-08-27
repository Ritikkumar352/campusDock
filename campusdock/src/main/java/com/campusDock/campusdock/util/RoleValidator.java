package com.campusDock.campusdock.util;

import com.campusDock.campusdock.entity.Enum.UserRole;
import com.campusDock.campusdock.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

@Component
public class RoleValidator {

    private final JwtService jwtService;

    public RoleValidator(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    public boolean hasAccess(
            HttpServletRequest request,
            UserRole... allowedRoles  // like a dyunamic array but not an array
    ) {
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return false; // no valid token
        }

        String token = authHeader.substring(7); // remove "Bearer "
        String role = jwtService.extractRole(token);
        System.out.println(role);

        if (role == null) {
            return false;
        }

        // simple loop instead of streams
        for (UserRole allowedRole : allowedRoles) {
            if (allowedRole.name().equals(role)) {
                return true;
            }
        }
        return false;
    }
}
