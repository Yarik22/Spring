package com.popov.main.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import com.popov.main.data.Entertainment;
import com.popov.main.service.EntertainmentService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/webflux/entertainments")
@RequiredArgsConstructor
public class EntertainmentController {

    private final EntertainmentService entertainmentService;

    @GetMapping
    public Flux<Entertainment> getAllEntertainments() {
        return entertainmentService.getAllEntertainments();
    }

    @GetMapping("/{id}")
    public Mono<Entertainment> getEntertainmentById(@PathVariable String id) {
        return entertainmentService.getEntertainmentById(id);
    }

    @PostMapping
    public Mono<Entertainment> createEntertainment(@RequestBody Entertainment entertainment) {
        return entertainmentService.createEntertainment(entertainment);
    }

    @PutMapping("/{id}")
    public Mono<Entertainment> updateEntertainment(@PathVariable String id, @RequestBody Entertainment entertainment) {
        return entertainmentService.updateEntertainment(id, entertainment);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deleteEntertainment(@PathVariable String id) {
        return entertainmentService.deleteEntertainment(id);
    }
}
