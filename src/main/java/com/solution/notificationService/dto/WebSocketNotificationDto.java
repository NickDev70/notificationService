package com.solution.notificationService.dto;

import com.solution.notificationService.model.NotificationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WebSocketNotificationDto {
    private Integer id;
    private Integer userId;
    private String eventTitle;
    private String eventDescription;
    private LocalDateTime scheduledFor;
    private NotificationStatus status;
    private LocalDateTime sentAt;
    private String message;
}