package com.solution.notificationService.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.solution.notificationService.model.*;
import com.solution.notificationService.repository.*;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationShedulerService {
    
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final NotififcationService notificationService;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");

    @Transactional
    public void distributeNotificationsForEvent(Event event) {
        List<User> users = userRepository.findAll();
        
        for (User user : users) {
            if (isInNotificationPeriod(event.getOccuredAt(), user)) {
                sendImmediateNotification(user, event);
            } else {
                LocalDateTime scheduledTime = findNextAvailablePeriod(event.getOccuredAt(), user);
                notificationService.createNotification(user, event, scheduledTime);
                log.info("Notification for {} scheduled for {}", user.getFullName(), scheduledTime);
            }
        }
    }

    private void sendImmediateNotification(User user, Event event) {
        Notification notification = notificationService.createNotification(user, event, LocalDateTime.now());
        notificationService.sendNotification(notification);
        
        log.info("{} Notification sent to user {} with message: {}", 
                LocalDateTime.now().format(formatter),
                user.getFullName(),
                event.getMessage());
    }

    @Scheduled(fixedRate = 300000)
    @Transactional
    public void processScheduledNotifications() {
        LocalDateTime now = LocalDateTime.now();
        List<Notification> notificationsToSend = notificationRepository
                .findByStatusAndScheduledForBefore(NotificationStatus.SCHEDULED, now);
        
        for (Notification notification : notificationsToSend) {
            notificationService.sendNotification(notification);
            
            log.info("{} Notification sent to user {} with message: {}", 
                    LocalDateTime.now().format(formatter),
                    notification.getUser().getFullName(),
                    notification.getEvent().getMessage());
        }
    }

    private boolean isInNotificationPeriod(LocalDateTime eventTime, User user) {
        DayOfWeek eventDay = eventTime.getDayOfWeek();
        LocalTime eventTimeOfDay = eventTime.toLocalTime();
        
        return user.getNotificationPeriods().stream()
                .anyMatch(period -> period.getDayOfWeek() == eventDay && 
                         isTimeInPeriod(eventTimeOfDay, period));
    }

    private boolean isTimeInPeriod(LocalTime time, NotificationPeriod period) {
        return !time.isBefore(period.getStartTime()) && 
               !time.isAfter(period.getEndTime());
    }

    private LocalDateTime findNextAvailablePeriod(LocalDateTime fromTime, User user) {
        LocalDateTime current = fromTime;
        
        for (int day = 0; day < 7; day++) {
            DayOfWeek dayOfWeek = current.getDayOfWeek();
            
            for (NotificationPeriod period : user.getNotificationPeriods()) {
                if (period.getDayOfWeek() == dayOfWeek) {
                    LocalDateTime candidateTime = current.toLocalDate()
                            .atTime(period.getStartTime());
                    
                    if (candidateTime.isAfter(fromTime)) {
                        return candidateTime;
                    }
                }
            }
            current = current.plusDays(1);
        }
        
        return fromTime.plusWeeks(1);
    }
}