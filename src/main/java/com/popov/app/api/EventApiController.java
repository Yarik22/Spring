package com.popov.app.api;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.annotation.JsonFormat;

import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/events")
public class EventApiController {

    private final WebClient webClient;

    public EventApiController(WebClient.Builder webClientBuilder, @Value("${api.base-url}") String baseUrl) {
        this.webClient = webClientBuilder.baseUrl(baseUrl).build();
    }

    @GetMapping
    public Flux<ResponseEntity<Event>> getAllEvents() {
        return webClient.get()
                .uri("/events")
                .retrieve()
                .bodyToFlux(Event.class)
                .map(ResponseEntity::ok);
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Event>> getEvent(@PathVariable String id) {
        return webClient.get()
                .uri("/events/{id}", id)
                .retrieve()
                .bodyToMono(Event.class)
                .map(ResponseEntity::ok);
    }

    @PostMapping
    public Mono<ResponseEntity<Event>> createEvent(@RequestBody Event event) {
        return webClient.post()
                .uri("/events")
                .bodyValue(event)
                .retrieve()
                .bodyToMono(Event.class)
                .map(ResponseEntity::ok);
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteEvent(@PathVariable String id) {
        return webClient.delete()
                .uri("/events/{id}", id)
                .retrieve()
                .bodyToMono(Void.class)
                .map(ResponseEntity::ok);
    }
public static class Event {
        private String name;
        private String description;
        private String location;
        private String date;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }
    }
}
