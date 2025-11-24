package com.projeto.authservice.controller;

import com.projeto.authservice.controller.dto.ProfileResponse;
import com.projeto.authservice.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProfileControllerTest {
    @Mock
    private UserService userService;
    @InjectMocks
    private ProfileController profileController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testProfile() {
        Jwt jwt = mock(Jwt.class);
        ProfileResponse profileResponse = mock(ProfileResponse.class);
        when(userService.readProfile(jwt)).thenReturn(profileResponse);
        ResponseEntity<ProfileResponse> response = profileController.profile(jwt);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(profileResponse, response.getBody());
    }
}
