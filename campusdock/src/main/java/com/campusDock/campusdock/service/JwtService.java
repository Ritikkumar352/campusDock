package com.campusDock.campusdock.service;

import com.campusDock.campusdock.entity.Enum.UserRole;
import com.campusDock.campusdock.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import javax.management.relation.Role;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Component
public class JwtService
{
    @Value("${jwt.secret}")
    private String jwtSecret;

    // 1. Generate token
    public String generateToken(User user) {
        return Jwts.builder()
                .setSubject(user.getEmail())
                .claim("userId", user.getId().toString())
                .claim("collegeId",user.getCollege().getId().toString())
                .claim("role",user.getRole().name())
                .setIssuedAt(new Date())
                .setExpiration(Date.from(Instant.now().plus(10, ChronoUnit.DAYS)))
//                .setExpiration(new Date(System.currentTimeMillis()+1000 * 60 * 60 * 24*10))    //This is 10 day time
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // 2. Extract username/email
    public String extractEmail(String token)
    {
        return getClaims(token).getSubject();
    }

    // 3. Extract full claims
    private Claims getClaims(String token) {
        return  Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
//                .parseClaimsJwt(token)  // this is for unsigned
                .parseClaimsJws(token)
                .getBody();
    }

    // 4. Validate token
    public boolean isTokenValid(String token, User user) {
        final String email = extractEmail(token);
        return (email.equals(user.getEmail()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return getClaims(token).getExpiration().before(new Date());
    }
    // 5. Key decoder
    private Key getSigningKey() {
        byte[] keyBytes=jwtSecret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }
    public String extractUserId(String token) {
        return getClaims(token).get("userId", String.class);
    }

    public String extractCollegeId(String token) {
        return getClaims(token).get("collegeId", String.class);
    }

    public String extractRole(String token) {
        return getClaims(token).get("role", String.class);
    }

//    String collegeId = jwtService.extractCollegeId(token);
//    String userId = jwtService.extractUserId(token);
//    String role = jwtService.extractRole(token);

    // TODO :- check above all methods are valid or not





}



////        String role = (String) request.getAttribute("userRole");
//// Get Authorization header
//String authHeader = request.getHeader("Authorization");
//        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
//        return new ResponseEntity<>("Missing or invalid Authorization header", HttpStatus.UNAUTHORIZED);
//        }
//
//// Extract JWT
//String token = authHeader.substring(7);
//
//// Extract role from JWT
//String role = jwtService.extractRole(token);
//        System.out.println("YOUR ROLE IS :" + role);
//        if (role == null || (!role.equals("ADMIN") && !role.equals("SUPER_ADMIN") && !role.equals("CANTEEN_OWNER"))) {
//        System.out.println("YOUR ROLE IS :" + role);
//            return new ResponseEntity<>("Access Denied: Insufficient role.", HttpStatus.FORBIDDEN);
//        }