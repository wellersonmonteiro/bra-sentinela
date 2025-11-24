package com.projeto.apigateway.controller;

import com.projeto.apigateway.config.AdminCrudServiceClient;
import com.projeto.apigateway.controller.dto.AdminCreateRequest;
import com.projeto.apigateway.controller.dto.AdminUpdateRequest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/admin")
@RequiredArgsConstructor
public class AdminCrudController {
    private final AdminCrudServiceClient adminCrudServiceClient;

    @PostMapping
    public ResponseEntity<?> createAdmin(@RequestBody AdminCreateRequest request) {
        Object admin = adminCrudServiceClient.createAdmin(request);
        return ResponseEntity.ok(admin);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getAdmin(@PathVariable Long id) {
        Object admin = adminCrudServiceClient.getAdmin(id);
        return ResponseEntity.ok(admin);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateAdmin(@PathVariable Long id, @RequestBody AdminUpdateRequest request) {
        Object admin = adminCrudServiceClient.updateAdmin(id, request);
        return ResponseEntity.ok(admin);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAdmin(@PathVariable Long id) {
        adminCrudServiceClient.deleteAdmin(id);
        return ResponseEntity.noContent().build();
    }
}
