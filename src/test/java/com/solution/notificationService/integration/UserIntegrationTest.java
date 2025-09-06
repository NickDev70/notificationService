package com.solution.notificationService.integration;

import com.solution.notificationService.dto.UserDto;
import com.solution.notificationService.repository.UserRepository;
import com.solution.notificationService.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class UserIntegrationTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    void createAndRetrieveUser_ShouldWorkCorrectly() {
        // Given
        UserDto userDto = new UserDto(null, "Тестовый пользователь", "");

        // When
        UserDto createdUser = userService.createUser(userDto);
        UserDto retrievedUser = userService.getUserById(createdUser.getId().longValue());

        // Then
        assertNotNull(createdUser.getId());
        assertEquals("Тестовый пользователь", createdUser.getFullName());
        assertEquals(createdUser.getId(), retrievedUser.getId());
        assertEquals(createdUser.getFullName(), retrievedUser.getFullName());
    }

    @Test
    void updateUser_ShouldUpdateCorrectly() {
        // Given
        UserDto userDto = new UserDto(null, "Исходное имя", "");
        UserDto createdUser = userService.createUser(userDto);
        
        UserDto updateDto = new UserDto(createdUser.getId(), "Обновленное имя", "");

        // When
        UserDto updatedUser = userService.updateUser(createdUser.getId().longValue(), updateDto);

        // Then
        assertEquals("Обновленное имя", updatedUser.getFullName());
        assertEquals(createdUser.getId(), updatedUser.getId());
    }

    @Test
    void deleteUser_ShouldDeleteCorrectly() {
        // Given
        UserDto userDto = new UserDto(null, "Пользователь для удаления", "");
        UserDto createdUser = userService.createUser(userDto);

        // When
        userService.deleteUser(createdUser.getId().longValue());

        // Then
        assertThrows(RuntimeException.class, 
            () -> userService.getUserById(createdUser.getId().longValue()));
    }

    @Test
    void getAllUsers_ShouldReturnAllUsers() {
        // Given
        UserDto user1 = new UserDto(null, "Пользователь 1", "");
        UserDto user2 = new UserDto(null, "Пользователь 2", "");
        
        userService.createUser(user1);
        userService.createUser(user2);

        // When
        List<UserDto> allUsers = userService.getAllusers();

        // Then
        assertEquals(2, allUsers.size());
    }
}
