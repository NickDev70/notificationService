package com.solution.notificationService.service;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.solution.notificationService.dto.WebSocketNotificationDto;
import com.solution.notificationService.model.Notification;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WebSocketService {
    
    private final SimpMessagingTemplate messagingTemplate;
    private final Logger log = LoggerFactory.getLogger(WebSocketService.class);
    
    public void sendNotification(Notification notification) {
        try {
            WebSocketNotificationDto dto = WebSocketNotificationDto.builder()
                    .id(notification.getId())
                    .userId(notification.getUser().getId())
                    .eventTitle(notification.getEvent().getMessage())
                    .eventDescription(notification.getEvent().getMessage())
                    .scheduledFor(notification.getScheduledFor())
                    .status(notification.getStatus())
                    .sentAt(notification.getSentAt())
                    .message(buildNotificationMessage(notification))
                    .build();
            
            String userDestination = "/topic/user/" + notification.getUser().getId();
            messagingTemplate.convertAndSend(userDestination, dto);
            
            messagingTemplate.convertAndSend("/topic/notifications", dto);
            
            log.info("WebSocket уведомление отправлено пользователю {}: {}", 
                    notification.getUser().getId(), dto.getMessage());
                    
        } catch (Exception e) {
            log.error("Ошибка отправки WebSocket уведомления: {}", e.getMessage(), e);
            throw new RuntimeException("Не удалось отправить WebSocket уведомление", e);
        }
    }
    
    private String buildNotificationMessage(Notification notification) {
        return String.format("Уведомление: %s", notification.getEvent().getMessage());
    }
}