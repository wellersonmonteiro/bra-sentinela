package com.projeto.apigateway.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.projeto.apigateway.config.AdminServiceClient;
import com.projeto.apigateway.dto.AdminLoginRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/admin/auth")
@RequiredArgsConstructor
public class AdminAuthController {
    private final AdminServiceClient adminServiceClient;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AdminLoginRequest request) {
        Object tokenResponse = adminServiceClient.login(request);
        return ResponseEntity.ok(tokenResponse);
    }
}
