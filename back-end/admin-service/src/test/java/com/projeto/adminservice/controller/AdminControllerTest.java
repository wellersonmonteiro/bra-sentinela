package com.projeto.adminservice.controller;

import com.projeto.adminservice.entity.Admin;
import com.projeto.adminservice.service.AdminService;
import com.projeto.adminservice.dto.AdminCreateRequest;
import com.projeto.adminservice.dto.AdminUpdateRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AdminControllerTest {
    @Mock
    private AdminService adminService;
    @InjectMocks
    private AdminController adminController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateAdmin() {
        Admin admin = new Admin();
        AdminCreateRequest req = new AdminCreateRequest();
        req.setUsername("user");
        req.setEmail("email");
        req.setPassword("pass");
        when(adminService.createAdmin("user", "email", "pass")).thenReturn(admin);
        ResponseEntity<Admin> response = adminController.createAdmin(req);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(admin, response.getBody());
    }

    @Test
    void testGetAdminFound() {
        Admin admin = new Admin();
        when(adminService.getAdminById(1L)).thenReturn(Optional.of(admin));
        ResponseEntity<Admin> response = adminController.getAdmin(1L);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(admin, response.getBody());
    }

    @Test
    void testGetAdminNotFound() {
        when(adminService.getAdminById(1L)).thenReturn(Optional.empty());
        ResponseEntity<Admin> response = adminController.getAdmin(1L);
        assertEquals(404, response.getStatusCodeValue());
        assertNull(response.getBody());
    }

    @Test
    void testUpdateAdmin() {
        Admin admin = new Admin();
        AdminUpdateRequest req = new AdminUpdateRequest();
        req.setUsername("user");
        req.setEmail("email");
        when(adminService.updateAdmin(1L, "user", "email")).thenReturn(admin);
        ResponseEntity<Admin> response = adminController.updateAdmin(1L, req);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(admin, response.getBody());
    }

    @Test
    void testDeleteAdmin() {
        doNothing().when(adminService).deleteAdmin(1L);
        ResponseEntity<Void> response = adminController.deleteAdmin(1L);
        assertEquals(204, response.getStatusCodeValue());
        assertNull(response.getBody());
    }
}
