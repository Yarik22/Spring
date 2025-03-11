package com.popov.main.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import com.popov.main.data.Event;
import com.popov.main.service.EventService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/webflux/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @GetMapping
    public Flux<Event> getAllEvents() {
        return eventService.getAllEvents();
    }

    @GetMapping("/{id}")
    public Mono<Event> getEventById(@PathVariable String id) {
        return eventService.getEventById(id);
    }

    @PostMapping
    public Mono<Event> createEvent(@RequestBody Event event) {
        return eventService.createEvent(event);
    }

    @PutMapping("/{id}")
    public Mono<Event> updateEvent(@PathVariable String id, @RequestBody Event event) {
        return eventService.updateEvent(id, event);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deleteEvent(@PathVariable String id) {
        return eventService.deleteEvent(id);
    }
}
