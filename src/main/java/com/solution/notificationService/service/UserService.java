package com.solution.notificationService.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.solution.notificationService.dto.UserDto;
import com.solution.notificationService.model.NotificationPeriod;
import com.solution.notificationService.model.User;
import com.solution.notificationService.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public List<UserDto> getAllusers() {
        return userRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public UserDto getUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        return convertToDto(user);
    }

    @Transactional
    public UserDto createUser(UserDto userDto) {
        User user = convertToEntity(userDto);
        User savedUser = userRepository.save(user);
        return convertToDto(savedUser);
    }

    @Transactional
    public UserDto updateUser(Long id, UserDto userDto) {
        User exitingUser = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        exitingUser.setFullName(userDto.getFullName());
        User updatedUser = userRepository.save(exitingUser);
        return convertToDto(updatedUser);
    }

    @Transactional
    public void deleteUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        userRepository.delete(user);
    }

    private UserDto convertToDto(User user) {
            String notificationPeriods = "";

            if (user.getNotificationPeriods() != null) {
                List<String> periodStrings = new ArrayList<>();

                for (NotificationPeriod period : user.getNotificationPeriods()) {
                    String periodString = period.getDayOfWeek() + " " +
                                          period.getStartTime() + " - " + 
                                          period.getEndTime();
                    periodStrings.add(periodString);
                }

                notificationPeriods = String.join(", ", periodStrings);
            }
        return new UserDto(user.getId(), user.getFullName(), notificationPeriods);
    }

    private User convertToEntity(UserDto userDto) {
        User user = new User();
        user.setId(userDto.getId());
        user.setFullName(userDto.getFullName());
        return user;
    }
    
}
