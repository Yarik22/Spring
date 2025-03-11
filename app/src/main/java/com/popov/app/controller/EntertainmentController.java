package com.popov.app.controller;

import com.popov.app.model.Entertainment;
import com.popov.app.service.EntertainmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("api/entertainments")
public class EntertainmentController {

    private final EntertainmentService entertainmentService;

    public EntertainmentController(EntertainmentService entertainmentService) {
        this.entertainmentService = entertainmentService;
    }

    @GetMapping
    public List<Entertainment> getAllEntertainment() {
        return entertainmentService.getAllEntertainment();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Entertainment> getEntertainmentById(@PathVariable UUID id) {
        Optional<Entertainment> entertainment = entertainmentService.getEntertainmentById(id);
        return entertainment.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public Entertainment createEntertainment(@RequestBody Entertainment entertainment) {
        return entertainmentService.saveEntertainment(entertainment);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Entertainment> updateEntertainment(@PathVariable UUID id, @RequestBody Entertainment entertainmentDetails) {
        try {
            Entertainment updatedEntertainment = entertainmentService.updateEntertainment(id, entertainmentDetails);
            return ResponseEntity.ok(updatedEntertainment);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEntertainment(@PathVariable UUID id) {
        entertainmentService.deleteEntertainment(id);
        return ResponseEntity.noContent().build();
    }
}
