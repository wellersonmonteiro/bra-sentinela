package com.projeto.apigateway.config;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.projeto.apigateway.dto.AdminLoginRequest;

@FeignClient(name = "admin-service", url = "http://localhost:3009/v1/admin/auth")
public interface AdminServiceClient {
    @PostMapping("/login")
    Object login(@RequestBody AdminLoginRequest request);
}
