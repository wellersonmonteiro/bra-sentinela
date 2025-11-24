package com.projeto.apigateway.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.projeto.apigateway.config.AdminCrudServiceClient;
import com.projeto.apigateway.controller.dto.AdminCreateRequest;
import com.projeto.apigateway.controller.dto.AdminUpdateRequest;

class AdminCrudControllerTest {
    @Mock
    private AdminCrudServiceClient adminCrudServiceClient;
    @InjectMocks
    private AdminCrudController adminCrudController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateAdmin() {
        Object admin = new Object();
        AdminCreateRequest req = new AdminCreateRequest();
        req.setUsername("user");
        req.setEmail("email");
        req.setPassword("pass");
        when(adminCrudServiceClient.createAdmin(req)).thenReturn(admin);
        ResponseEntity<?> response = adminCrudController.createAdmin(req);
        assertEquals(200, response.getStatusCode().value());
        assertEquals(admin, response.getBody());
    }

    @Test
    void testGetAdmin() {
        Object admin = new Object();
        when(adminCrudServiceClient.getAdmin(1L)).thenReturn(admin);
        ResponseEntity<?> response = adminCrudController.getAdmin(1L);
        assertEquals(200, response.getStatusCode().value());
        assertEquals(admin, response.getBody());
    }

    @Test
    void testUpdateAdmin() {
        Object admin = new Object();
        AdminUpdateRequest req = new AdminUpdateRequest();
        req.setUsername("user");
        req.setEmail("email");
        when(adminCrudServiceClient.updateAdmin(1L, req)).thenReturn(admin);
        ResponseEntity<?> response = adminCrudController.updateAdmin(1L, req);
        assertEquals(200, response.getStatusCode().value());
        assertEquals(admin, response.getBody());
    }

    @Test
    void testDeleteAdmin() {
        doNothing().when(adminCrudServiceClient).deleteAdmin(1L);
        ResponseEntity<?> response = adminCrudController.deleteAdmin(1L);
        assertEquals(204, response.getStatusCode().value());
        assertNull(response.getBody());
    }
}
