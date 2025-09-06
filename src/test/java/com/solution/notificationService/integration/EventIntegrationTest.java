package com.solution.notificationService.integration;

import com.solution.notificationService.dto.EventDto;
import com.solution.notificationService.repository.EventRepository;
import com.solution.notificationService.service.EventService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class EventIntegrationTest {

    @Autowired
    private EventService eventService;

    @Autowired
    private EventRepository eventRepository;

    private LocalDateTime testDateTime;

    @BeforeEach
    void setUp() {
        eventRepository.deleteAll();
        testDateTime = LocalDateTime.of(2024, 1, 15, 10, 30);
    }

    @Test
    void createAndRetrieveEvent_ShouldWorkCorrectly() {
        // Given
        EventDto eventDto = new EventDto(null, "Тестовое событие", testDateTime);

        // When
        EventDto createdEvent = eventService.createEvent(eventDto);
        Optional<EventDto> retrievedEvent = eventService.getEventById(createdEvent.getId());

        // Then
        assertNotNull(createdEvent.getId());
        assertEquals("Тестовое событие", createdEvent.getMessage());
        assertTrue(retrievedEvent.isPresent());
        assertEquals(createdEvent.getId(), retrievedEvent.get().getId());
    }

    @Test
    void createEvent_WithoutOccurredAt_ShouldSetCurrentTime() {
        // Given
        EventDto eventDto = new EventDto(null, "Событие без времени", null);

        // When
        EventDto createdEvent = eventService.createEvent(eventDto);

        // Then
        assertNotNull(createdEvent.getOccurredAt());
        assertTrue(createdEvent.getOccurredAt().isBefore(LocalDateTime.now().plusMinutes(1)));
    }

    @Test
    void updateEvent_ShouldUpdateCorrectly() {
        // Given
        EventDto eventDto = new EventDto(null, "Исходное событие", testDateTime);
        EventDto createdEvent = eventService.createEvent(eventDto);
        
        EventDto updateDto = new EventDto(createdEvent.getId(), "Обновленное событие", testDateTime);

        // When
        Optional<EventDto> updatedEvent = eventService.updateEvent(createdEvent.getId(), updateDto);

        // Then
        assertTrue(updatedEvent.isPresent());
        assertEquals("Обновленное событие", updatedEvent.get().getMessage());
        assertEquals(createdEvent.getId(), updatedEvent.get().getId());
    }

    @Test
    void deleteEvent_ShouldDeleteCorrectly() {
        // Given
        EventDto eventDto = new EventDto(null, "Событие для удаления", testDateTime);
        EventDto createdEvent = eventService.createEvent(eventDto);

        // When
        boolean deleted = eventService.deleteEvent(createdEvent.getId());

        // Then
        assertTrue(deleted);
        assertFalse(eventService.getEventById(createdEvent.getId()).isPresent());
    }

    @Test
    void searchEventsByMessage_ShouldFindMatchingEvents() {
        // Given
        EventDto event1 = new EventDto(null, "Важное сообщение", testDateTime);
        EventDto event2 = new EventDto(null, "Обычное сообщение", testDateTime);
        EventDto event3 = new EventDto(null, "Важная информация", testDateTime);
        
        eventService.createEvent(event1);
        eventService.createEvent(event2);
        eventService.createEvent(event3);

        // When
        List<EventDto> foundEvents = eventService.searchEventsByMessage("Важн");

        // Then
        assertEquals(2, foundEvents.size());
    }

    @Test
    void getEventsByDateRange_ShouldReturnEventsInRange() {
        // Given
        LocalDateTime start = LocalDateTime.of(2024, 1, 1, 0, 0);
        LocalDateTime end = LocalDateTime.of(2024, 1, 31, 23, 59);
        LocalDateTime outsideRange = LocalDateTime.of(2024, 2, 1, 0, 0);
        
        EventDto event1 = new EventDto(null, "Событие в диапазоне", testDateTime);
        EventDto event2 = new EventDto(null, "Событие вне диапазона", outsideRange);
        
        eventService.createEvent(event1);
        eventService.createEvent(event2);

        // When
        List<EventDto> eventsInRange = eventService.getEventByDataRange(start, end);

        // Then
        assertEquals(1, eventsInRange.size());
        assertEquals("Событие в диапазоне", eventsInRange.get(0).getMessage());
    }
}
