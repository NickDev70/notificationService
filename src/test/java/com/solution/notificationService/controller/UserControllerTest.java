package com.solution.notificationService.controller;

import com.solution.notificationService.dto.UserDto;
import com.solution.notificationService.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private UserDto testUserDto;

    @BeforeEach
    void setUp() {
        testUserDto = new UserDto(1, "Иван Иванов", "MONDAY 09:00 - 18:00");
    }

    @Test
    void getAllUsers_ShouldReturnAllUsers() {
        List<UserDto> users = Arrays.asList(testUserDto);
        when(userService.getAllusers()).thenReturn(users);

        ResponseEntity<List<UserDto>> response = userController.getAllUsers();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        List<UserDto> body = response.getBody();
        assertNotNull(body);
        assertEquals(1, body.size());
        assertEquals("Иван Иванов", body.get(0).getFullName());
        verify(userService).getAllusers();
    }

    @Test
    void getUserById_ShouldReturnUser() {
        when(userService.getUserById(1L)).thenReturn(testUserDto);

        ResponseEntity<UserDto> response = userController.getUserById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        UserDto body = response.getBody();
        assertNotNull(body);
        assertEquals("Иван Иванов", body.getFullName());
        verify(userService).getUserById(1L);
    }

    @Test
    void createUser_ShouldCreateAndReturnUser() {
        UserDto newUserDto = new UserDto(null, "Петр Петров", "");
        UserDto createdUserDto = new UserDto(2, "Петр Петров", "");
        when(userService.createUser(newUserDto)).thenReturn(createdUserDto);

        ResponseEntity<UserDto> response = userController.createUser(newUserDto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        UserDto body = response.getBody();
        assertNotNull(body);
        assertEquals("Петр Петров", body.getFullName());
        verify(userService).createUser(newUserDto);
    }

    @Test
    void updateUser_ShouldUpdateAndReturnUser() {
        UserDto updateDto = new UserDto(1, "Иван Обновленный", "");
        when(userService.updateUser(1L, updateDto)).thenReturn(updateDto);

        ResponseEntity<UserDto> response = userController.updateUser(1L, updateDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        UserDto body = response.getBody();
        assertNotNull(body);
        assertEquals("Иван Обновленный", body.getFullName());
        verify(userService).updateUser(1L, updateDto);
    }

    @Test
    void deleteUser_ShouldDeleteUser() {
        doNothing().when(userService).deleteUser(1L);

        ResponseEntity<Void> response = userController.deleteUser(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(userService).deleteUser(1L);
    }
}
