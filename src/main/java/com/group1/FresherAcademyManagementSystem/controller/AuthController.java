package com.group1.FresherAcademyManagementSystem.controller;

import com.group1.FresherAcademyManagementSystem.configs.LogoutService;
import com.group1.FresherAcademyManagementSystem.dtos.UserProfileDto;
import com.group1.FresherAcademyManagementSystem.exceptions.CustomSuccessHandler;
import com.group1.FresherAcademyManagementSystem.services.AuthService;
import com.group1.FresherAcademyManagementSystem.services.CloudinaryUploadService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@CrossOrigin("http://localhost:5173/")
public class AuthController {

    private final AuthService authService;

    private final LogoutService logoutService;

    private final CloudinaryUploadService cloudinaryUploadConfig;

    @Operation(
            summary = "Register a new account",
            description = "To register a new account, all information must be filled out and cannot be left blank. " +
                    "Upon successful registration, the response will include an access token and a refresh token",
            tags = {"Authentication"})
    @PostMapping("/register")
    public ResponseEntity<Object> register(@RequestBody @Valid RegisterRequest request) {
        return CustomSuccessHandler.responseBuilder("Successfully Register", HttpStatus.OK, authService.register(request));
    }

    @Operation(
            summary = "Login in to the system",
            description = "Login into the system requires all information to be provided, " +
                    "and validations will be performed. The response will include an access token and a refresh token",
            tags = {"Authentication"})
    @PostMapping("/signin")
    public ResponseEntity<Object> signIn(@RequestBody @Valid AuthenticationRequest authenticate) {
        return CustomSuccessHandler.responseBuilder("Successfully SignIn", HttpStatus.OK, authService.authenticate(authenticate));
    }

    @Operation(
            summary = "Refresh token if expired",
            description = "If the current JWT token has expired or been revoked, you can refresh it using this method",
            tags = {"Authentication"})
    @PostMapping("/refresh-token")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        authService.refreshToken(request, response);
    }

    @Operation(
            summary = "Logout of the system",
            description = "Logout of the system, bearer is required",
            tags = {"Authentication"})
    @PostMapping("/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        logoutService.logout(request, response, authentication);
    }

    @Operation(
            summary = "View user profile",
            description = "View current user information after logging into the system. Passwords, tokens, etc., " +
                    "will not be displayed",
            tags = {"Account"})
    @GetMapping("/account")
    public ResponseEntity<Object> getCurrentLoginUser(HttpServletRequest request) {
        return authService.getUserInformation(request);
    }

    @Operation(
            summary = "Update user profile information",
            description = "Update current user information after logging into the system.",
            tags = {"Account"})
    @PutMapping(value = "/account/update_profile")
    public ResponseEntity<Object> updateCurrentLoginUser(@NotNull Long id,
                                                         @RequestBody UserProfileDto userProfileDto) {

        UserProfileDto updatedUserProfileDto = authService.updateUserInformation(id, userProfileDto);
        if (updatedUserProfileDto != null) {
            return ResponseEntity.ok().body("User profile information updated successfully");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update user profile information");
        }
    }

    @Operation(
            summary = "Update user profile picture",
            description = "Update current user profile picture after logging into the system.",
            tags = {"Account"})
    @PostMapping(value = "/account/image_upload", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Object> updateUserProfilePicture(@NotNull Long id,
                                                           @RequestParam(value = "profilePicture", required = false) MultipartFile profilePicture) throws IOException {

        if (profilePicture != null && !profilePicture.isEmpty()) {
            String imageURLMain = cloudinaryUploadConfig.uploadFile(profilePicture);
            if (imageURLMain != null) {
                authService.updateUserProfilePicture(id, imageURLMain);
                return ResponseEntity.ok().body("User profile picture updated successfully");
            }
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update user profile picture");
    }
}
