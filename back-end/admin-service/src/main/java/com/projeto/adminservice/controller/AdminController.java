package com.projeto.adminservice.controller;

import com.projeto.adminservice.entity.Admin;
import com.projeto.adminservice.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;
import com.projeto.adminservice.dto.AdminCreateRequest;
import com.projeto.adminservice.dto.AdminUpdateRequest;

@RestController
@RequestMapping("v1/admin")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;

    @PostMapping
    public ResponseEntity<Admin> createAdmin(@RequestBody AdminCreateRequest request) {
        Admin admin = adminService.createAdmin(request.getUsername(), request.getEmail(), request.getPassword());
        return ResponseEntity.ok(admin);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Admin> getAdmin(@PathVariable Long id) {
        Optional<Admin> admin = adminService.getAdminById(id);
        return admin.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Admin> updateAdmin(@PathVariable Long id, @RequestBody AdminUpdateRequest request) {
        Admin admin = adminService.updateAdmin(id, request.getUsername(), request.getEmail());
        return ResponseEntity.ok(admin);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAdmin(@PathVariable Long id) {
        adminService.deleteAdmin(id);
        return ResponseEntity.noContent().build();
    }
}
