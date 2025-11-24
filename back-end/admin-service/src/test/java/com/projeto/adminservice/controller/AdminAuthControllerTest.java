package com.projeto.adminservice.controller;

import com.projeto.adminservice.client.AuthServiceClient;
import com.projeto.adminservice.entity.Admin;
import com.projeto.adminservice.service.AdminService;
import com.projeto.adminservice.dto.AdminLoginRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AdminAuthControllerTest {
    @Mock
    private AdminService adminService;
    @Mock
    private AuthServiceClient authServiceClient;
    @InjectMocks
    private AdminAuthController adminAuthController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testLoginSuccess() {
        Admin admin = new Admin();
        AdminLoginRequest req = new AdminLoginRequest();
        req.setUsername("user");
        req.setPassword("pass");
        when(adminService.getAdminByUsername("user")).thenReturn(Optional.of(admin));
        when(adminService.checkPassword("pass", admin.getPasswordHash())).thenReturn(true);
        Object tokenResponse = new Object();
        when(authServiceClient.login(any())).thenReturn(tokenResponse);
        ResponseEntity<?> response = adminAuthController.login(req);
        assertEquals(200, response.getStatusCode().value());
        assertEquals(tokenResponse, response.getBody());
    }

    @Test
    void testLoginInvalidUser() {
        AdminLoginRequest req = new AdminLoginRequest();
        req.setUsername("user");
        req.setPassword("pass");
        when(adminService.getAdminByUsername("user")).thenReturn(Optional.empty());
        ResponseEntity<?> response = adminAuthController.login(req);
        assertEquals(401, response.getStatusCode().value());
        assertEquals("Invalid credentials", response.getBody());
    }

    @Test
    void testLoginInvalidPassword() {
        Admin admin = new Admin();
        AdminLoginRequest req = new AdminLoginRequest();
        req.setUsername("user");
        req.setPassword("pass");
        when(adminService.getAdminByUsername("user")).thenReturn(Optional.of(admin));
        when(adminService.checkPassword("pass", admin.getPasswordHash())).thenReturn(false);
        ResponseEntity<?> response = adminAuthController.login(req);
        assertEquals(401, response.getStatusCode().value());
        assertEquals("Invalid credentials", response.getBody());
    }
}
