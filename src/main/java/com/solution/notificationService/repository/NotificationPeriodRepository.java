package com.solution.notificationService.repository;

import com.solution.notificationService.model.NotificationPeriod;
import com.solution.notificationService.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationPeriodRepository extends JpaRepository<NotificationPeriod, Integer> {
    List<NotificationPeriod> findByUser(User user);
}
