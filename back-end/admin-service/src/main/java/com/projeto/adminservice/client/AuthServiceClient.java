package com.projeto.adminservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "auth-service", url = "http://localhost:3001/auth")
public interface AuthServiceClient {
    @PostMapping("/login")
    Object login(@RequestBody AuthServiceLoginRequest request);

    class AuthServiceLoginRequest {
        public String username;
        public String password;
        public AuthServiceLoginRequest(String username, String password) {
            this.username = username;
            this.password = password;
        }
    }
}
