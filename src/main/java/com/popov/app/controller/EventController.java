package com.popov.app.controller;

import com.popov.app.dto.event.EventCreateDTO;
import com.popov.app.dto.event.EventResponseDTO;
import com.popov.app.dto.event.EventUpdateDTO;
import com.popov.app.model.Event;
import com.popov.app.service.EventService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/events")
public class EventController {

        
    @Autowired
    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @Operation(summary = "Get all events", description = "Retrieve a list of all events")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of events"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public ResponseEntity<List<EventResponseDTO>> getAllEvents() {
        List<Event> events = eventService.getAllEvents();
        List<EventResponseDTO> eventDtos = events.stream()
                .map(this::convertToEventResponseDTO)
                .collect(Collectors.toList());
        return new ResponseEntity<>(eventDtos, HttpStatus.OK);
    }

    @Operation(summary = "Get an event by ID", description = "Retrieve a specific event by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved event"),
            @ApiResponse(responseCode = "404", description = "Event not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{id}")
    public ResponseEntity<EventResponseDTO> getEventById(
            @Parameter(description = "ID of the event to be retrieved") @PathVariable UUID id) {
        Optional<Event> event = eventService.getEventById(id);
        return event.map(e -> new ResponseEntity<>(convertToEventResponseDTO(e), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Operation(summary = "Create a new event", description = "Create a new event with provided details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Event successfully created"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public ResponseEntity<EventResponseDTO> createEvent(@RequestBody EventCreateDTO eventCreateDTO) {
        Event event = convertToEvent(eventCreateDTO);
        Event createdEvent = eventService.createEvent(event);
        return new ResponseEntity<>(convertToEventResponseDTO(createdEvent), HttpStatus.CREATED);
    }

    @Operation(summary = "Update an event", description = "Update an existing event by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Event successfully updated"),
            @ApiResponse(responseCode = "404", description = "Event not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PatchMapping("/{id}")
    public ResponseEntity<EventResponseDTO> updateEvent(
            @Parameter(description = "ID of the event to be updated") @PathVariable UUID id,
            @RequestBody EventUpdateDTO eventUpdateDTO) {
        Event event = convertToEvent(eventUpdateDTO);
        event.setId(id);
        Event updatedEvent = eventService.updateEvent(id, event);
        return updatedEvent != null ?
                new ResponseEntity<>(convertToEventResponseDTO(updatedEvent), HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @Operation(summary = "Delete an event", description = "Delete an event by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Event successfully deleted"),
            @ApiResponse(responseCode = "404", description = "Event not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(
            @Parameter(description = "ID of the event to be deleted") @PathVariable UUID id) {
        return eventService.deleteEvent(id) ?
                new ResponseEntity<>(HttpStatus.NO_CONTENT) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    private Event convertToEvent(EventCreateDTO eventCreateDTO) {
        Event event = new Event();
        event.setName(eventCreateDTO.getName());
        event.setDescription(eventCreateDTO.getDescription());
        event.setLocation(eventCreateDTO.getLocation());
        event.setDate(eventCreateDTO.getDate());
        return event;
    }

    private Event convertToEvent(EventUpdateDTO eventUpdateDTO) {
        Event event = new Event();
        event.setName(eventUpdateDTO.getName());
        event.setDescription(eventUpdateDTO.getDescription());
        event.setLocation(eventUpdateDTO.getLocation());
        event.setDate(eventUpdateDTO.getDate());
        return event;
    }

    private EventResponseDTO convertToEventResponseDTO(Event event) {
        EventResponseDTO eventResponseDTO = new EventResponseDTO();
        eventResponseDTO.setId(event.getId());
        eventResponseDTO.setName(event.getName());
        eventResponseDTO.setDescription(event.getDescription());
        eventResponseDTO.setLocation(event.getLocation());
        eventResponseDTO.setDate(event.getDate());
        return eventResponseDTO;
    }
}
