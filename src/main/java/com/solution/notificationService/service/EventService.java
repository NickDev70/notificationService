package com.solution.notificationService.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.solution.notificationService.repository.EventRepository;
import com.solution.notificationService.model.Event;
import com.solution.notificationService.dto.EventDto;

import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventService {
    private final EventRepository eventRepository;

    @Transactional(readOnly = true)
    public List<EventDto> getAllEvents() {
        return eventRepository.findAll().stream()
                                        .map(this::convertToDto)
                                        .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<EventDto> getEventById(Integer id) {
        return eventRepository.findById(id)
                              .map(this::convertToDto);
    }

    public EventDto createEvent(EventDto eventDto) {
        Event event = convertToEntity(eventDto);
        event.setOccuredAt(eventDto.getOccurredAt() != null ?
                           eventDto.getOccurredAt() : LocalDateTime.now());
        Event savedEvent = eventRepository.save(event);
        return convertToDto(savedEvent);
    }

    public Optional<EventDto> updateEvent(Integer id, EventDto eventDto) {
        return eventRepository.findById(id)
                .map(existingEvent -> {
                    existingEvent.setMessage(eventDto.getMessage());
                    existingEvent.setOccuredAt(eventDto.getOccurredAt() != null ? 
                                             eventDto.getOccurredAt() : LocalDateTime.now());
                    Event updatedEvent = eventRepository.save(existingEvent);
                    return convertToDto(updatedEvent);
                });
    }

    public boolean deleteEvent(Integer id) {
        if (eventRepository.existsById(id)) {
            eventRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Transactional(readOnly = true)
    public List<EventDto> searchEventsByMessage(String keyword) {
        return eventRepository.findByMessageContaining(keyword).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<EventDto> getEventByDataRange(LocalDateTime start, LocalDateTime end) {
        return eventRepository.findByOccuredAtBetween(start, end).stream()
                                .map(this::convertToDto)
                                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<EventDto> getEventsAfterDate(LocalDateTime dateTime) {
        return eventRepository.findByOccuredAtAfter(dateTime).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private EventDto convertToDto(Event event) {
        EventDto dto = new EventDto();
        dto.setId(event.getId());
        dto.setMessage(event.getMessage());
        dto.setOccurredAt(event.getOccuredAt());
        return dto;
    }

    private Event convertToEntity(EventDto dto) {
        Event event = new Event();
        event.setId(dto.getId());
        event.setMessage(dto.getMessage());
        event.setOccuredAt(dto.getOccurredAt());
        return event;
    }
}
