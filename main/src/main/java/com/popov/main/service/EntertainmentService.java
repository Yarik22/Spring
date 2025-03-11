package com.popov.main.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.popov.main.data.Entertainment;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class EntertainmentService {

    private final WebClient webClient;

    public Flux<Entertainment> getAllEntertainments() {
        return webClient.get()
                .uri("/entertainments")
                .retrieve()
                .bodyToFlux(Entertainment.class);
    }

    public Mono<Entertainment> getEntertainmentById(String id) {
        return webClient.get()
                .uri("entertainments/{id}", id)
                .retrieve()
                .bodyToMono(Entertainment.class);
    }

    public Mono<Entertainment> createEntertainment(Entertainment entertainment) {
        return webClient.post()
                .uri("/entertainments")
                .bodyValue(entertainment)
                .retrieve()
                .bodyToMono(Entertainment.class);
    }
}
