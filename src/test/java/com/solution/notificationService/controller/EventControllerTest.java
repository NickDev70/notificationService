package com.solution.notificationService.controller;

import com.solution.notificationService.dto.EventDto;
import com.solution.notificationService.service.EventService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventControllerTest {

    @Mock
    private EventService eventService;

    @InjectMocks
    private EventController eventController;

    private EventDto testEventDto;
    private LocalDateTime testDateTime;

    @BeforeEach
    void setUp() {
        testDateTime = LocalDateTime.of(2024, 1, 15, 10, 30);
        testEventDto = new EventDto(1, "Тестовое событие", testDateTime);
    }

    @Test
    void getAllEvents_ShouldReturnAllEvents() {
        // Given
        List<EventDto> events = Arrays.asList(testEventDto);
        when(eventService.getAllEvents()).thenReturn(events);

        // When
        ResponseEntity<List<EventDto>> response = eventController.getAllEvents();

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        List<EventDto> body = response.getBody();
        assertNotNull(body);
        assertEquals(1, body.size());
        assertEquals("Тестовое событие", body.get(0).getMessage());
        verify(eventService).getAllEvents();
    }

    @Test
    void getEventById_WhenEventExists_ShouldReturnEvent() {
        // Given
        when(eventService.getEventById(1)).thenReturn(Optional.of(testEventDto));

        // When
        ResponseEntity<EventDto> response = eventController.getEventById(1);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        EventDto body = response.getBody();
        assertNotNull(body);
        assertEquals("Тестовое событие", body.getMessage());
        verify(eventService).getEventById(1);
    }

    @Test
    void getEventById_WhenEventNotExists_ShouldReturnNotFound() {
        // Given
        when(eventService.getEventById(999)).thenReturn(Optional.empty());

        // When
        ResponseEntity<EventDto> response = eventController.getEventById(999);

        // Then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(eventService).getEventById(999);
    }

    @Test
    void createEvent_ShouldCreateAndReturnEvent() {
        // Given
        EventDto newEventDto = new EventDto(null, "Новое событие", testDateTime);
        when(eventService.createEvent(newEventDto)).thenReturn(testEventDto);

        // When
        ResponseEntity<EventDto> response = eventController.createEvent(newEventDto);

        // Then
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        EventDto body = response.getBody();
        assertNotNull(body);
        assertEquals("Тестовое событие", body.getMessage());
        verify(eventService).createEvent(newEventDto);
    }

    @Test
    void updateEvent_WhenEventExists_ShouldUpdateAndReturnEvent() {
        // Given
        EventDto updateDto = new EventDto(1, "Обновленное событие", testDateTime);
        when(eventService.updateEvent(1, updateDto)).thenReturn(Optional.of(updateDto));

        // When
        ResponseEntity<EventDto> response = eventController.updateEvent(1, updateDto);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        EventDto body = response.getBody();
        assertNotNull(body);
        assertEquals("Обновленное событие", body.getMessage());
        verify(eventService).updateEvent(1, updateDto);
    }

    @Test
    void updateEvent_WhenEventNotExists_ShouldReturnNotFound() {
        // Given
        EventDto updateDto = new EventDto(999, "Несуществующее событие", testDateTime);
        when(eventService.updateEvent(999, updateDto)).thenReturn(Optional.empty());

        // When
        ResponseEntity<EventDto> response = eventController.updateEvent(999, updateDto);

        // Then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(eventService).updateEvent(999, updateDto);
    }

    @Test
    void deleteEvent_WhenEventExists_ShouldReturnNoContent() {
        // Given
        when(eventService.deleteEvent(1)).thenReturn(true);

        // When
        ResponseEntity<Void> response = eventController.deleteEvent(1);

        // Then
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(eventService).deleteEvent(1);
    }

    @Test
    void deleteEvent_WhenEventNotExists_ShouldReturnNotFound() {
        // Given
        when(eventService.deleteEvent(999)).thenReturn(false);

        // When
        ResponseEntity<Void> response = eventController.deleteEvent(999);

        // Then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(eventService).deleteEvent(999);
    }

    @Test
    void searchEventsByMessage_ShouldReturnMatchingEvents() {
        // Given
        List<EventDto> events = Arrays.asList(testEventDto);
        when(eventService.searchEventsByMessage("тест")).thenReturn(events);

        // When
        ResponseEntity<List<EventDto>> response = eventController.searchEventsByMessage("тест");

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        List<EventDto> body = response.getBody();
        assertNotNull(body);
        assertEquals(1, body.size());
        verify(eventService).searchEventsByMessage("тест");
    }

    @Test
    void getEventsByDateRange_ShouldReturnEventsInRange() {
        // Given
        LocalDateTime start = LocalDateTime.of(2024, 1, 1, 0, 0);
        LocalDateTime end = LocalDateTime.of(2024, 1, 31, 23, 59);
        List<EventDto> events = Arrays.asList(testEventDto);
        when(eventService.getEventByDataRange(start, end)).thenReturn(events);

        // When
        ResponseEntity<List<EventDto>> response = eventController.getEventsByDateRange(start, end);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        List<EventDto> body = response.getBody();
        assertNotNull(body);
        assertEquals(1, body.size());
        verify(eventService).getEventByDataRange(start, end);
    }

    @Test
    void getEventsAfterDate_ShouldReturnEventsAfterDate() {
        // Given
        LocalDateTime afterDate = LocalDateTime.of(2024, 1, 1, 0, 0);
        List<EventDto> events = Arrays.asList(testEventDto);
        when(eventService.getEventsAfterDate(afterDate)).thenReturn(events);

        // When
        ResponseEntity<List<EventDto>> response = eventController.getEventsAfterDate(afterDate);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        List<EventDto> body = response.getBody();
        assertNotNull(body);
        assertEquals(1, body.size());
        verify(eventService).getEventsAfterDate(afterDate);
    }
}
