package com.solution.notificationService.service;

import com.solution.notificationService.dto.WebSocketNotificationDto;
import com.solution.notificationService.model.Event;
import com.solution.notificationService.model.Notification;
import com.solution.notificationService.model.NotificationStatus;
import com.solution.notificationService.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class WebSocketServiceTest {

    @Mock
    private SimpMessagingTemplate messagingTemplate;

    @InjectMocks
    private WebSocketService webSocketService;

    private Notification testNotification;
    private User testUser;
    private Event testEvent;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1);
        testUser.setFullName("Иван Иванов");

        testEvent = new Event();
        testEvent.setId(1);
        testEvent.setMessage("Тестовое событие");
        testEvent.setOccuredAt(LocalDateTime.now());

        testNotification = new Notification();
        testNotification.setId(1);
        testNotification.setUser(testUser);
        testNotification.setEvent(testEvent);
        testNotification.setScheduledFor(LocalDateTime.now());
        testNotification.setStatus(NotificationStatus.SCHEDULED);
    }

    @Test
    void sendNotification_ShouldSendWebSocketMessage() {
        // When
        webSocketService.sendNotification(testNotification);

        // Then
        verify(messagingTemplate).convertAndSend(
            eq("/topic/notifications"), 
            any(WebSocketNotificationDto.class)
        );
    }
}
