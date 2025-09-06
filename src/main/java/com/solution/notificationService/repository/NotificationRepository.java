package com.solution.notificationService.repository;

import java.util.List;
import org.springframework.stereotype.Repository;
import com.solution.notificationService.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import com.solution.notificationService.model.NotificationStatus;
import com.solution.notificationService.model.User;
import java.time.LocalDateTime;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Integer> {
    List<Notification> findByStatusAndScheduledForBefore(NotificationStatus status, LocalDateTime dateTime);
    List<Notification> findByUserAndEvent(User user, com.solution.notificationService.model.Event event);
}

