package com.projeto.authservice.controller;

import com.projeto.authservice.controller.dto.LoginRequest;
import com.projeto.authservice.controller.dto.LoginResponse;
import com.projeto.authservice.controller.dto.LogoutRequest;
import com.projeto.authservice.controller.dto.RefreshRequest;
import com.projeto.authservice.service.LoginService;
import com.projeto.authservice.service.LogoutService;
import com.projeto.authservice.service.RefreshTokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthControllerTest {
    @Mock
    private LoginService loginService;
    @Mock
    private RefreshTokenService refreshTokenService;
    @Mock
    private LogoutService logoutService;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testLogin() {
        LoginRequest req = mock(LoginRequest.class);
        LoginResponse resp = mock(LoginResponse.class);
        when(loginService.login(any(), any())).thenReturn(resp);
        ResponseEntity<LoginResponse> response = authController.login(req);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(resp, response.getBody());
    }

    @Test
    void testRefresh() {
        RefreshRequest req = mock(RefreshRequest.class);
        LoginResponse resp = mock(LoginResponse.class);
        when(refreshTokenService.refreshToken(any())).thenReturn(resp);
        ResponseEntity<LoginResponse> response = authController.login(req);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(resp, response.getBody());
    }

    @Test
    void testLogout() {
        Jwt jwt = mock(Jwt.class);
        when(jwt.getSubject()).thenReturn("1");
        LogoutRequest req = mock(LogoutRequest.class);
        doNothing().when(logoutService).logout(anyLong(), any());
        ResponseEntity<LoginResponse> response = authController.login(jwt, req);
        assertEquals(204, response.getStatusCodeValue());
        assertNull(response.getBody());
    }
}
