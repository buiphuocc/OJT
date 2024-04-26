package com.group1.FresherAcademyManagementSystem.services;

import com.group1.FresherAcademyManagementSystem.controller.AuthenticationRequest;
import com.group1.FresherAcademyManagementSystem.controller.AuthenticationResponse;
import com.group1.FresherAcademyManagementSystem.controller.RegisterRequest;
import com.group1.FresherAcademyManagementSystem.dtos.UserProfileDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

public interface AuthService {
    AuthenticationResponse register(RegisterRequest request);

    AuthenticationResponse authenticate(AuthenticationRequest authenticate);

    void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException;

    ResponseEntity<Object> getUserInformation(HttpServletRequest request);

    UserProfileDto updateUserInformation(Long id, UserProfileDto userInformation);

    void updateUserProfilePicture(Long id, String profilePictureUrl);
}
