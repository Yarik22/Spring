package com.popov.main.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import com.popov.main.data.Event;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class EventService {

    private final WebClient webClient;

    public Flux<Event> getAllEvents() {
        return webClient.get()
                .uri("api/events")
                .retrieve()
                .bodyToFlux(Event.class);
    }

    public Mono<Event> getEventById(String id) {
        return webClient.get()
                .uri("api/events/{id}", id)
                .retrieve()
                .bodyToMono(Event.class);
    }

    public Mono<Event> createEvent(Event event) {
        return webClient.post()
                .uri("api/events")
                .bodyValue(event)
                .retrieve()
                .bodyToMono(Event.class);
    }

    public Mono<Event> updateEvent(String id, Event event) {
        return webClient.put()
                .uri("api/events/{id}", id)
                .bodyValue(event)
                .retrieve()
                .bodyToMono(Event.class);
    }

    public Mono<Void> deleteEvent(String id) {
        return webClient.delete()
                .uri("api/events/{id}", id)
                .retrieve()
                .bodyToMono(Void.class);
    }
}
