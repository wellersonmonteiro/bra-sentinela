package com.projeto.adminservice.controller;

import com.projeto.adminservice.entity.Admin;
import com.projeto.adminservice.service.AdminService;

import com.projeto.adminservice.client.AuthServiceClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;
import com.projeto.adminservice.dto.AdminLoginRequest;

@RestController
@RequestMapping("v1/admin/auth")

@RequiredArgsConstructor
public class AdminAuthController {
    private final AdminService adminService;
    private final AuthServiceClient authServiceClient;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AdminLoginRequest request) {
        Optional<Admin> adminOpt = adminService.getAdminByUsername(request.getUsername());
        if (adminOpt.isEmpty()) {
            return ResponseEntity.status(401).body("Invalid credentials");
        }
        Admin admin = adminOpt.get();
        if (!adminService.checkPassword(request.getPassword(), admin.getPasswordHash())) {
            return ResponseEntity.status(401).body("Invalid credentials");
        }
        // Call auth-service to generate tokens
        Object tokenResponse = authServiceClient.login(new AuthServiceClient.AuthServiceLoginRequest(request.getUsername(), request.getPassword()));
        return ResponseEntity.ok(tokenResponse);
    }
}
