package com.solution.notificationService.service;

import com.solution.notificationService.model.Event;
import com.solution.notificationService.model.Notification;
import com.solution.notificationService.model.NotificationStatus;
import com.solution.notificationService.model.User;
import com.solution.notificationService.repository.NotificationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private WebSocketService webSocketService;

    @InjectMocks
    private NotififcationService notificationService;

    private User testUser;
    private Event testEvent;
    private Notification testNotification;
    private LocalDateTime testDateTime;

    @BeforeEach
    void setUp() {
        testDateTime = LocalDateTime.of(2024, 1, 15, 10, 30);
        
        testUser = new User();
        testUser.setId(1);
        testUser.setFullName("Иван Иванов");

        testEvent = new Event();
        testEvent.setId(1);
        testEvent.setMessage("Тестовое событие");
        testEvent.setOccuredAt(testDateTime);

        testNotification = new Notification();
        testNotification.setId(1);
        testNotification.setUser(testUser);
        testNotification.setEvent(testEvent);
        testNotification.setScheduledFor(testDateTime);
        testNotification.setStatus(NotificationStatus.SCHEDULED);
    }

    @Test
    void createNotification_ShouldCreateAndReturnNotification() {
        when(notificationRepository.save(any(Notification.class))).thenReturn(testNotification);

        Notification result = notificationService.createNotification(testUser, testEvent, testDateTime);

        assertNotNull(result);
        assertEquals(testUser, result.getUser());
        assertEquals(testEvent, result.getEvent());
        assertEquals(testDateTime, result.getScheduledFor());
        assertEquals(NotificationStatus.SCHEDULED, result.getStatus());
        verify(notificationRepository).save(any(Notification.class));
    }

    @Test
    void sendNotification_WhenSuccessful_ShouldUpdateStatusToSent() {
        doNothing().when(webSocketService).sendNotification(testNotification);
        when(notificationRepository.save(any(Notification.class))).thenReturn(testNotification);

        notificationService.sendNotification(testNotification);

        assertEquals(NotificationStatus.SENT, testNotification.getStatus());
        assertNotNull(testNotification.getSentAt());
        verify(webSocketService).sendNotification(testNotification);
        verify(notificationRepository).save(testNotification);
    }

    @Test
    void sendNotification_WhenWebSocketFails_ShouldUpdateStatusToFailed() {
        doThrow(new RuntimeException("WebSocket error")).when(webSocketService).sendNotification(testNotification);
        when(notificationRepository.save(any(Notification.class))).thenReturn(testNotification);

        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> notificationService.sendNotification(testNotification));
        
        assertEquals("WebSocket error", exception.getMessage());
        assertEquals(NotificationStatus.FAILED, testNotification.getStatus());
        verify(webSocketService).sendNotification(testNotification);
        verify(notificationRepository).save(testNotification);
    }
}
