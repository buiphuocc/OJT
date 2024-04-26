package com.group1.FresherAcademyManagementSystem.services;

import org.springframework.security.core.userdetails.UserDetails;

import java.util.Map;


public interface JwtService {
    String extractUsername(String jwtToken);

    boolean isTokenValid(String token, UserDetails userDetails);

    String generateToken(Map<String, Object> extraClaims, UserDetails userDetails);

    String generateToken(UserDetails userDetails);

    String generateRefreshToken(UserDetails userDetails);
}
