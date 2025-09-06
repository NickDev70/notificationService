package com.solution.notificationService.service;

import com.solution.notificationService.dto.UserDto;
import com.solution.notificationService.model.NotificationPeriod;
import com.solution.notificationService.model.User;
import com.solution.notificationService.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User testUser;
    private NotificationPeriod testPeriod;

    @BeforeEach
    void setUp() {
        testPeriod = new NotificationPeriod();
        testPeriod.setDayOfWeek(DayOfWeek.MONDAY);
        testPeriod.setStartTime(LocalTime.of(9, 0));
        testPeriod.setEndTime(LocalTime.of(18, 0));

        testUser = new User();
        testUser.setId(1);
        testUser.setFullName("Иван Иванов");
        testUser.setNotificationPeriods(Arrays.asList(testPeriod));
    }

    @Test
    void getAllUsers_ShouldReturnAllUsers() {
        List<User> users = Arrays.asList(testUser);
        when(userRepository.findAll()).thenReturn(users);
        List<UserDto> result = userService.getAllusers();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Иван Иванов", result.get(0).getFullName());
        assertEquals("MONDAY 09:00 - 18:00", result.get(0).getNotificationPeriods());
        verify(userRepository).findAll();
    }
    @Test
    void getUserById_WhenUserExists_ShouldReturnUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        UserDto result = userService.getUserById(1L);

        assertNotNull(result);
        assertEquals("Иван Иванов", result.getFullName());
        verify(userRepository).findById(1L);
    }

    @Test
    void getUserById_WhenUserNotExists_ShouldThrowException() {
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> userService.getUserById(999L));
        assertEquals("User not found with id: 999", exception.getMessage());
        verify(userRepository).findById(999L);
    }

    @Test
    void createUser_ShouldCreateAndReturnUser() {
        UserDto newUserDto = new UserDto(null, "Петр Петров", "");
        User savedUser = new User();
        savedUser.setId(2);
        savedUser.setFullName("Петр Петров");
        
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        UserDto result = userService.createUser(newUserDto);

        assertNotNull(result);
        assertEquals("Петр Петров", result.getFullName());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void updateUser_WhenUserExists_ShouldUpdateAndReturnUser() {
        UserDto updateDto = new UserDto(1, "Иван Обновленный", "");
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        UserDto result = userService.updateUser(1L, updateDto);

        assertNotNull(result);
        verify(userRepository).findById(1L);
        verify(userRepository).save(any(User.class));
    }

    @Test
    void updateUser_WhenUserNotExists_ShouldThrowException() {
        UserDto updateDto = new UserDto(999, "Несуществующий", "");
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> userService.updateUser(999L, updateDto));
        assertEquals("User not found with id: 999", exception.getMessage());
        verify(userRepository).findById(999L);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void deleteUser_WhenUserExists_ShouldDeleteUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        userService.deleteUser(1L);

        verify(userRepository).findById(1L);
        verify(userRepository).delete(testUser);
    }

    @Test
    void deleteUser_WhenUserNotExists_ShouldThrowException() {
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> userService.deleteUser(999L));
        assertEquals("User not found with id: 999", exception.getMessage());
        verify(userRepository).findById(999L);
        verify(userRepository, never()).delete(any(User.class));
    }
}
