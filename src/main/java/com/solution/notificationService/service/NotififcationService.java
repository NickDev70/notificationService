package com.solution.notificationService.service;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.solution.notificationService.model.Event;
import com.solution.notificationService.model.Notification;
import com.solution.notificationService.model.NotificationStatus;
import com.solution.notificationService.model.User;
import com.solution.notificationService.repository.NotificationRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotififcationService {
    private final NotificationRepository notificationRepository;
    private final WebSocketService webSocketService;
    private final Logger log = LoggerFactory.getLogger(NotififcationService.class);

    @Transactional
    public Notification createNotification(User user, Event event, LocalDateTime scheduledFor) {
        Notification notification = new Notification();
        notification.setUser(user);
        notification.setEvent(event);
        notification.setScheduledFor(scheduledFor);
        notification.setStatus(NotificationStatus.SCHEDULED);

        Notification saved = notificationRepository.save(notification);
        log.info("Notification created: {}", saved);
        return saved;
    }

    @Transactional
    public void sendNotification(Notification notification) {
        try {
            webSocketService.sendNotification(notification);
            
            notification.setStatus(NotificationStatus.SENT);
            notification.setSentAt(LocalDateTime.now());
            notificationRepository.save(notification);
            
            log.info("Уведомление {} успешно отправлено", notification.getId());
        } catch (Exception e) {
            notification.setStatus(NotificationStatus.FAILED);
            notificationRepository.save(notification);
            
            log.error("Ошибка отправки уведомления {}: {}", notification.getId(), e.getMessage());
            throw e;
        }
    }
}
