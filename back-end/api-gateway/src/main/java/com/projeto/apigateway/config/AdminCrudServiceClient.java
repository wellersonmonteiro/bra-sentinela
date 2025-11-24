package com.projeto.apigateway.config;

// No import needed for Admin
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import com.projeto.apigateway.controller.dto.AdminCreateRequest;
import com.projeto.apigateway.controller.dto.AdminUpdateRequest;

@FeignClient(name = "admin-crud-service", url = "http://localhost:3009/v1/admin")
public interface AdminCrudServiceClient {
    @PostMapping
    Object createAdmin(@RequestBody AdminCreateRequest request);

    @GetMapping("/{id}")
    Object getAdmin(@PathVariable("id") Long id);

    @PutMapping("/{id}")
    Object updateAdmin(@PathVariable("id") Long id, @RequestBody AdminUpdateRequest request);

    @DeleteMapping("/{id}")
    void deleteAdmin(@PathVariable("id") Long id);
}
