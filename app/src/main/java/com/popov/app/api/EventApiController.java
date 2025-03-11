package com.popov.app.api;

import com.popov.app.dto.event.EventCreateDTO;
import com.popov.app.dto.event.EventResponseDTO;
import com.popov.app.dto.event.EventUpdateDTO;
import com.popov.app.model.Event;
import com.popov.app.service.EventService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/events")
public class EventApiController {


    @Autowired
    private final EventService eventService;

    public EventApiController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping
    public ResponseEntity<List<EventResponseDTO>> getAllEvents() {
        List<EventResponseDTO> eventDtos = eventService.getAllEvents().stream()
                .map(this::convertToEventResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(eventDtos);
    }

    @PostMapping
    public ResponseEntity<EventResponseDTO> createEvent(@RequestBody @Valid EventCreateDTO eventCreateDTO) {
        Event event = convertToEvent(eventCreateDTO);
        Event createdEvent = eventService.createEvent(event);
        EventResponseDTO eventResponseDTO = convertToEventResponseDTO(createdEvent);
        return ResponseEntity.status(201).body(eventResponseDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventResponseDTO> getEventById(@PathVariable UUID id) {
        Event event = eventService.getEventById(id)
                .orElseThrow(() -> new IllegalArgumentException("Event not found with ID: " + id));
        return ResponseEntity.ok(convertToEventResponseDTO(event));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EventResponseDTO> updateEvent(
            @PathVariable UUID id,
            @RequestBody @Valid EventUpdateDTO eventUpdateDTO) {

        Event event = convertToEvent(eventUpdateDTO);
        Event updatedEvent = eventService.updateEvent(id, event);
        return ResponseEntity.ok(convertToEventResponseDTO(updatedEvent));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable UUID id) {
        eventService.deleteEvent(id);
        return ResponseEntity.noContent().build();
    }

    private Event convertToEvent(EventCreateDTO dto) {
        Event event = new Event();
        event.setName(dto.getName());
        event.setDescription(dto.getDescription());
        event.setLocation(dto.getLocation());
        event.setDate(dto.getDate());
        return event;
    }

    private Event convertToEvent(EventUpdateDTO dto) {
        Event event = new Event();
        event.setName(dto.getName());
        event.setDescription(dto.getDescription());
        event.setLocation(dto.getLocation());
        event.setDate(dto.getDate());
        return event;
    }


    private EventResponseDTO convertToEventResponseDTO(Event event) {
        EventResponseDTO dto = new EventResponseDTO();
        dto.setId(event.getId());
        dto.setName(event.getName());
        dto.setDescription(event.getDescription());
        dto.setLocation(event.getLocation());
        dto.setDate(event.getDate());
        return dto;
    }
}
