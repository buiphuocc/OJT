package com.group1.FresherAcademyManagementSystem.services.Impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.group1.FresherAcademyManagementSystem.configs.LogoutService;
import com.group1.FresherAcademyManagementSystem.controller.AuthenticationRequest;
import com.group1.FresherAcademyManagementSystem.controller.AuthenticationResponse;
import com.group1.FresherAcademyManagementSystem.controller.RegisterRequest;
import com.group1.FresherAcademyManagementSystem.dtos.UserProfileDto;
import com.group1.FresherAcademyManagementSystem.exceptions.CustomSuccessHandler;
import com.group1.FresherAcademyManagementSystem.models.Role;
import com.group1.FresherAcademyManagementSystem.models.UserEntity;
import com.group1.FresherAcademyManagementSystem.repositories.TokenRepository;
import com.group1.FresherAcademyManagementSystem.repositories.UserRepository;
import com.group1.FresherAcademyManagementSystem.services.AuthService;
import com.group1.FresherAcademyManagementSystem.services.JwtService;
import com.group1.FresherAcademyManagementSystem.token.Token;
import com.group1.FresherAcademyManagementSystem.token.TokenType;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "user")
public class AuthImplement implements AuthService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    private final TokenRepository tokenRepository;
    private static final Logger logger = LoggerFactory.getLogger(LogoutService.class);

    @Override
    public AuthenticationResponse register(RegisterRequest request) {
        var user = UserEntity.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .dob(request.getDob())
                .address(request.getAddress())
                .gender(request.getGender())
                .phone(request.getPhone())
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.ADMIN)
                .build();
        var savedUser = userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        saveUserToken(savedUser, jwtToken, refreshToken);

        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    private void saveUserToken(UserEntity user, String jwtToken, String jwtRefreshToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .refreshToken(jwtRefreshToken)
                .tokenType(TokenType.BEARER)
                .revoked(false)
                .expired(false)
                .build();
        tokenRepository.save(token);
    }

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest authenticate) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticate.getUsername(),
                        authenticate.getPassword()
                )
        );
        var user = userRepository.findByUsername(authenticate.getUsername())
                .orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);

        revokeAllUserToken(user);
        saveUserToken(user, jwtToken, refreshToken);

        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    private void revokeAllUserToken(UserEntity user) {
        var validUserToken = tokenRepository.findAllValidTokensByUser((user.getId()));
        if (validUserToken.isEmpty()) return;
        validUserToken.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserToken);
    }

    @Override
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshedToken;
        final String username;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType("text/plain");
            try {
                response.getWriter().write("No JWT token found in the request header");
            } catch (IOException e) {
                logger.error("Error writing unauthorized response", e);
            }
            return;
        }
        refreshedToken = authHeader.substring(7);
        username = jwtService.extractUsername(refreshedToken);

        final Token currentRefreshedToken = tokenRepository.findByRefreshToken(refreshedToken).get();

        if (username != null) {
            var user = this.userRepository.findByUsername(username)
                    .orElseThrow();
            if ((jwtService.isTokenValid(refreshedToken, user))
                    && !currentRefreshedToken.isRevoked() && !currentRefreshedToken.isExpired()) {
                var accessToken = jwtService.generateToken(user);
                var authResponse = AuthenticationResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshedToken)
                        .build();
                revokeAllUserToken(user);
                saveUserToken(user, accessToken, refreshedToken);
                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
                ResponseEntity.ok(authResponse);
            } else {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.setContentType("text/plain");
                try {
                    response.getWriter().write("JWT token has expired and revoked");
                } catch (IOException e) {
                    logger.error("Error writing unauthorized response", e);
                }
            }
            return;
        }
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("text/plain");
        try {
            response.getWriter().write("Unauthorized");
        } catch (IOException e) {
            logger.error("Error writing unauthorized response", e);
        }
//        ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
    }

    @Cacheable(value = "getUserInformation")
    @Override
    public ResponseEntity<Object> getUserInformation(HttpServletRequest request) {
        String token = extractTokenFromHeader(request);
        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No JWT token found in the request header");
        }

        final Token accessToken = tokenRepository.findByToken(token).orElse(null);
        if (accessToken == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid JWT token");
        }

        String username = jwtService.extractUsername(token);
        var user = userRepository.findByUsername(username).orElse(null);
        if (user == null || !jwtService.isTokenValid(token, user) || accessToken.isRevoked() || accessToken.isExpired()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("JWT token has expired and revoked");
        }

        return CustomSuccessHandler.responseBuilder("Successfully retrieved user information", HttpStatus.OK, user);
    }


    public String extractTokenFromHeader(HttpServletRequest request) {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }

    @CachePut(key = "#id")
    @Override
    public UserProfileDto updateUserInformation(Long id, UserProfileDto userProfileDto) {
        UserEntity user = userRepository.findById(id).orElse(null);
        if (user != null) {
            user.setFullName(userProfileDto.getFullName());
            user.setEmail(userProfileDto.getEmail());
            user.setDob(userProfileDto.getDob());
            user.setAddress(userProfileDto.getAddress());
            user.setGender(userProfileDto.getGender());
            user.setPhone(userProfileDto.getPhone());

            UserEntity updatedUser = userRepository.save(user);

            return mapToUserProfileDto(updatedUser);
        } else {
            return null;
        }
    }

    @Override
    public void updateUserProfilePicture(Long id, String profilePictureUrl) {
        UserEntity user = userRepository.findById(id).orElse(null);
        if (user != null) {
            user.setProfilePic(profilePictureUrl);
            UserEntity updatedUser = userRepository.save(user);
            mapToUserProfileDto(updatedUser);
        }
    }

    private UserProfileDto mapToUserProfileDto(UserEntity userEntity) {
        UserProfileDto userProfileDto = new UserProfileDto();
        userProfileDto.setId(userEntity.getId());
        userProfileDto.setFullName(userEntity.getFullName());
        userProfileDto.setEmail(userEntity.getEmail());
        userProfileDto.setDob(userEntity.getDob());
        userProfileDto.setAddress(userEntity.getAddress());
        userProfileDto.setGender(userEntity.getGender());
        userProfileDto.setPhone(userProfileDto.getPhone());

        return userProfileDto;
    }
}
