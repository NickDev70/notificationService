package com.solution.notificationService.service;

import com.solution.notificationService.dto.EventDto;
import com.solution.notificationService.model.Event;
import com.solution.notificationService.repository.EventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventServiceTest {

    @Mock
    private EventRepository eventRepository;

    @InjectMocks
    private EventService eventService;

    private Event testEvent;
    private LocalDateTime testDateTime;

    @BeforeEach
    void setUp() {
        testDateTime = LocalDateTime.of(2024, 1, 15, 10, 30);
        
        testEvent = new Event();
        testEvent.setId(1);
        testEvent.setMessage("Тестовое событие");
        testEvent.setOccuredAt(testDateTime);
    }

    @Test
    void getAllEvents_ShouldReturnAllEvents() {
        // Given
        List<Event> events = Arrays.asList(testEvent);
        when(eventRepository.findAll()).thenReturn(events);

        // When
        List<EventDto> result = eventService.getAllEvents();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Тестовое событие", result.get(0).getMessage());
        verify(eventRepository).findAll();
    }

    @Test
    void getEventById_WhenEventExists_ShouldReturnEvent() {
        // Given
        when(eventRepository.findById(1)).thenReturn(Optional.of(testEvent));

        // When
        Optional<EventDto> result = eventService.getEventById(1);

        // Then
        assertTrue(result.isPresent());
        assertEquals("Тестовое событие", result.get().getMessage());
        verify(eventRepository).findById(1);
    }

    @Test
    void getEventById_WhenEventNotExists_ShouldReturnEmpty() {
        // Given
        when(eventRepository.findById(999)).thenReturn(Optional.empty());

        // When
        Optional<EventDto> result = eventService.getEventById(999);

        // Then
        assertFalse(result.isPresent());
        verify(eventRepository).findById(999);
    }

    @Test
    void createEvent_WithOccurredAt_ShouldCreateEvent() {
        // Given
        EventDto newEventDto = new EventDto(null, "Новое событие", testDateTime);
        Event savedEvent = new Event();
        savedEvent.setId(2);
        savedEvent.setMessage("Новое событие");
        savedEvent.setOccuredAt(testDateTime);
        
        when(eventRepository.save(any(Event.class))).thenReturn(savedEvent);

        // When
        EventDto result = eventService.createEvent(newEventDto);

        // Then
        assertNotNull(result);
        assertEquals("Новое событие", result.getMessage());
        assertEquals(testDateTime, result.getOccurredAt());
        verify(eventRepository).save(any(Event.class));
    }

    @Test
    void createEvent_WithoutOccurredAt_ShouldSetCurrentTime() {
        // Given
        EventDto newEventDto = new EventDto(null, "Новое событие", null);
        Event savedEvent = new Event();
        savedEvent.setId(2);
        savedEvent.setMessage("Новое событие");
        savedEvent.setOccuredAt(LocalDateTime.now());
        
        when(eventRepository.save(any(Event.class))).thenAnswer(invocation -> {
            Event event = invocation.getArgument(0);
            event.setId(2);
            event.setOccuredAt(LocalDateTime.now());
            return event;
        });

        // When
        EventDto result = eventService.createEvent(newEventDto);

        // Then
        assertNotNull(result);
        assertEquals("Новое событие", result.getMessage());
        assertNotNull(result.getOccurredAt());
        verify(eventRepository).save(any(Event.class));
    }

    @Test
    void updateEvent_WhenEventExists_ShouldUpdateEvent() {
        // Given
        EventDto updateDto = new EventDto(1, "Обновленное событие", testDateTime);
        when(eventRepository.findById(1)).thenReturn(Optional.of(testEvent));
        when(eventRepository.save(any(Event.class))).thenReturn(testEvent);

        // When
        Optional<EventDto> result = eventService.updateEvent(1, updateDto);

        // Then
        assertTrue(result.isPresent());
        verify(eventRepository).findById(1);
        verify(eventRepository).save(any(Event.class));
    }

    @Test
    void updateEvent_WhenEventNotExists_ShouldReturnEmpty() {
        // Given
        EventDto updateDto = new EventDto(999, "Несуществующее событие", testDateTime);
        when(eventRepository.findById(999)).thenReturn(Optional.empty());

        // When
        Optional<EventDto> result = eventService.updateEvent(999, updateDto);

        // Then
        assertFalse(result.isPresent());
        verify(eventRepository).findById(999);
        verify(eventRepository, never()).save(any(Event.class));
    }

    @Test
    void deleteEvent_WhenEventExists_ShouldReturnTrue() {
        // Given
        when(eventRepository.existsById(1)).thenReturn(true);

        // When
        boolean result = eventService.deleteEvent(1);

        // Then
        assertTrue(result);
        verify(eventRepository).existsById(1);
        verify(eventRepository).deleteById(1);
    }

    @Test
    void deleteEvent_WhenEventNotExists_ShouldReturnFalse() {
        // Given
        when(eventRepository.existsById(999)).thenReturn(false);

        // When
        boolean result = eventService.deleteEvent(999);

        // Then
        assertFalse(result);
        verify(eventRepository).existsById(999);
        verify(eventRepository, never()).deleteById(any());
    }

    @Test
    void searchEventsByMessage_ShouldReturnMatchingEvents() {
        // Given
        List<Event> events = Arrays.asList(testEvent);
        when(eventRepository.findByMessageContaining("тест")).thenReturn(events);

        // When
        List<EventDto> result = eventService.searchEventsByMessage("тест");

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(eventRepository).findByMessageContaining("тест");
    }

    @Test
    void getEventByDataRange_ShouldReturnEventsInRange() {
        // Given
        LocalDateTime start = LocalDateTime.of(2024, 1, 1, 0, 0);
        LocalDateTime end = LocalDateTime.of(2024, 1, 31, 23, 59);
        List<Event> events = Arrays.asList(testEvent);
        when(eventRepository.findByOccuredAtBetween(start, end)).thenReturn(events);

        // When
        List<EventDto> result = eventService.getEventByDataRange(start, end);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(eventRepository).findByOccuredAtBetween(start, end);
    }

    @Test
    void getEventsAfterDate_ShouldReturnEventsAfterDate() {
        // Given
        LocalDateTime afterDate = LocalDateTime.of(2024, 1, 1, 0, 0);
        List<Event> events = Arrays.asList(testEvent);
        when(eventRepository.findByOccuredAtAfter(afterDate)).thenReturn(events);

        // When
        List<EventDto> result = eventService.getEventsAfterDate(afterDate);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(eventRepository).findByOccuredAtAfter(afterDate);
    }
}
