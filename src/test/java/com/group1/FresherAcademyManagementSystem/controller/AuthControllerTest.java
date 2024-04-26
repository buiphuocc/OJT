package com.group1.FresherAcademyManagementSystem.controller;

import com.group1.FresherAcademyManagementSystem.configs.LogoutService;
import com.group1.FresherAcademyManagementSystem.models.Role;
import com.group1.FresherAcademyManagementSystem.models.UserEntity;
import com.group1.FresherAcademyManagementSystem.services.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.time.LocalDate;
import java.util.LinkedHashMap;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.Silent.class)
public class AuthControllerTest {

    @InjectMocks
    private AuthController authController;

    @Mock
    private AuthService authService;

    @Mock
    private LogoutService logoutService;

    @Test
    public void testSuccessfulLogin() {
        AuthenticationRequest request = new AuthenticationRequest();
        request.setUsername("taikt08s");
        request.setPassword("Taikt08s");

        AuthenticationResponse expectedResponse = AuthenticationResponse.builder()
                .accessToken("expectAccessToken")
                .refreshToken("expectRefreshToken")
                .build();

        when(authService.authenticate(any(AuthenticationRequest.class)))
                .thenReturn(expectedResponse);

        ResponseEntity<?> actualResponseEntity = authController.signIn(request);

        assertEquals(HttpStatus.OK, actualResponseEntity.getStatusCode());
        assertNotNull(actualResponseEntity.getBody());

        LinkedHashMap<?, ?> actualResponseMap = (LinkedHashMap<?, ?>) actualResponseEntity.getBody();
        assertNotNull(actualResponseMap);

        assertEquals("Successfully SignIn", actualResponseMap.get("message"));
        assertEquals(HttpStatus.OK.value(), actualResponseMap.get("httpStatus"));

        String actualTimestamp = (String) actualResponseMap.get("timestamp");
        assertNotNull(actualTimestamp);
        assertTrue(actualTimestamp.startsWith("04/17/2024"));

        assertNotNull(actualResponseMap.get("data"));
        assertEquals(AuthenticationResponse.class, actualResponseMap.get("data").getClass());

        AuthenticationResponse actualResponseData = (AuthenticationResponse) actualResponseMap.get("data");
        assertEquals("expectAccessToken", actualResponseData.getAccessToken());
        assertEquals("expectRefreshToken", actualResponseData.getRefreshToken());
    }

    @Test
    public void testGetCurrentLoginUser() {
        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        when(mockRequest.getHeader("Authorization")).thenReturn("Bearer validAccessToken");

        UserEntity mockUser = new UserEntity();
        mockUser.setId(123L);
        mockUser.setUsername("taikt08s");
        mockUser.setEmail("user@example.com");
        mockUser.setDob(LocalDate.ofEpochDay(2001 - 4 - 4));
        mockUser.setAddress("Ho Chi minh");
        mockUser.setGender("Male");
        mockUser.setPhone("01234448958");
        mockUser.setRole(Role.ADMIN);

        when(authService.getUserInformation(any(HttpServletRequest.class)))
                .thenReturn(ResponseEntity.ok(mockUser));

        ResponseEntity<Object> responseEntity = authController.getCurrentLoginUser(mockRequest);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());

        UserEntity actualUser = (UserEntity) responseEntity.getBody();
        assertEquals("taikt08s", actualUser.getUsername());
        assertEquals("user@example.com", actualUser.getEmail());
        assertEquals(LocalDate.ofEpochDay(2001 - 4 - 4), actualUser.getDob());
        assertEquals("Ho Chi minh", actualUser.getAddress());
        assertEquals("Male", actualUser.getGender());
        assertEquals("01234448958", actualUser.getPhone());
        assertEquals(Role.ADMIN, actualUser.getRole());
    }

    @Test
    public void testLogout() {
        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        HttpServletResponse mockResponse = new MockHttpServletResponse();
        Authentication mockAuthentication = new UsernamePasswordAuthenticationToken("username", "password");

        authController.logout(mockRequest, mockResponse, mockAuthentication);

        verify(logoutService, times(1)).logout(mockRequest, mockResponse, mockAuthentication);

        assertEquals(HttpStatus.OK.value(), mockResponse.getStatus());
    }
}
