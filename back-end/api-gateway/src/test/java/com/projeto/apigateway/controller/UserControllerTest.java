package com.projeto.apigateway.controller;

import com.projeto.apigateway.controller.dto.UserCreateRequest;
import com.projeto.apigateway.controller.dto.UserCreateResponse;
import com.projeto.apigateway.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserControllerTest {
    @Mock
    private UserService userService;
    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetUserById() {
        UserCreateRequest request = mock(UserCreateRequest.class);
        UserCreateResponse responseObj = mock(UserCreateResponse.class);
        when(userService.createUser(request)).thenReturn(responseObj);
        ResponseEntity<UserCreateResponse> response = userController.getUserById(request);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(responseObj, response.getBody());
    }
}
