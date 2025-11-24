package com.projeto.userservice.controller;

import com.projeto.userservice.dto.UserDTO;
import com.projeto.userservice.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

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
    void testCreateUser() {
        UserDTO userDTO = new UserDTO();
        UserDTO createdUser = new UserDTO();
        when(userService.create(userDTO)).thenReturn(createdUser);

        ResponseEntity<UserDTO> response = userController.create(userDTO);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(createdUser, response.getBody());
    }

    @Test
    void testFindById() {
        UserDTO userDTO = new UserDTO();
        when(userService.findById(1)).thenReturn(userDTO);

        ResponseEntity<UserDTO> response = userController.findById(1);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userDTO, response.getBody());
    }

    @Test
    void testFindAll() {
        List<UserDTO> users = Arrays.asList(new UserDTO(), new UserDTO());
        when(userService.findAll()).thenReturn(users);

        ResponseEntity<List<UserDTO>> response = userController.findAll();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(users, response.getBody());
    }

    @Test
    void testFindByEmail() {
        UserDTO userDTO = new UserDTO();
        when(userService.findByEmail("test@example.com")).thenReturn(userDTO);

        ResponseEntity<UserDTO> response = userController.findByEmail("test@example.com");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userDTO, response.getBody());
    }

    @Test
    void testUpdateUser() {
        UserDTO userDTO = new UserDTO();
        UserDTO updatedUser = new UserDTO();
        when(userService.update(eq(1), any(UserDTO.class))).thenReturn(updatedUser);

        ResponseEntity<UserDTO> response = userController.update(1, userDTO);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedUser, response.getBody());
    }

    @Test
    void testDeleteUser() {
        doNothing().when(userService).delete(1);
        ResponseEntity<Void> response = userController.delete(1);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
    }
}
