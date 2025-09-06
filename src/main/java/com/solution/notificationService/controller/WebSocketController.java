package com.solution.notificationService.controller;

import com.solution.notificationService.dto.WebSocketNotificationDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
@Slf4j
public class WebSocketController {
    
    private final SimpMessagingTemplate messagingTemplate;
    
    @MessageMapping("/notification.send")
    @SendTo("/topic/notifications")
    public WebSocketNotificationDto sendNotification(WebSocketNotificationDto notification) {
        log.info("Received notification via WebSocket: {}", notification.getMessage());
        return notification;
    }
    
    @MessageMapping("/notification.user")
    public void sendNotificationToUser(WebSocketNotificationDto notification) {
        String destination = "/topic/user/" + notification.getUserId();
        messagingTemplate.convertAndSend(destination, notification);
        log.info("Notification sent to user {}: {}", 
                notification.getUserId(), notification.getMessage());
    }
}
