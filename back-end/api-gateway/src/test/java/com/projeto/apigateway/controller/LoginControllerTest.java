package com.projeto.apigateway.controller;

import com.projeto.apigateway.controller.dto.LoginRequest;
import com.projeto.apigateway.controller.dto.LoginResponse;
import com.projeto.apigateway.service.LoginService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LoginControllerTest {
    @Mock
    private LoginService loginService;
    @InjectMocks
    private LoginController loginController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testLogin() {
        LoginRequest request = mock(LoginRequest.class);
        LoginResponse responseObj = mock(LoginResponse.class);
        when(loginService.login(request)).thenReturn(responseObj);
        ResponseEntity<LoginResponse> response = loginController.login(request);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(responseObj, response.getBody());
    }
}
