package com.solution.notificationService.repository;

import com.solution.notificationService.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Integer> {
    
    List<Event> findByOccuredAtBetween(LocalDateTime start, LocalDateTime end);
    
    @Query("SELECT e FROM Event e WHERE e.message LIKE %:keyword%")
    List<Event> findByMessageContaining(@Param("keyword") String keyword);
    
    List<Event> findByOccuredAtAfter(LocalDateTime dateTime);
}
