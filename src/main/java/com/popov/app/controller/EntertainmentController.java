package com.popov.app.controller;

import com.popov.app.dto.entartainment.EntertainmentCreateDTO;
import com.popov.app.dto.entartainment.EntertainmentResponseDTO;
import com.popov.app.dto.entartainment.EntertainmentUpdateDTO;
import com.popov.app.model.Entertainment;
import com.popov.app.service.EntertainmentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/entertainment")
public class EntertainmentController {

        
    @Autowired
    private final EntertainmentService entertainmentService;

    public EntertainmentController(EntertainmentService entertainmentService) {
        this.entertainmentService = entertainmentService;
    }

    @GetMapping
    public ResponseEntity<List<EntertainmentResponseDTO>> getAllEntertainment() {
        List<Entertainment> entertainmentItems = entertainmentService.getAllEntertainments();
        List<EntertainmentResponseDTO> entertainmentDtos = entertainmentItems.stream()
                .map(this::convertToEntertainmentResponseDTO)
                .collect(Collectors.toList());
        return new ResponseEntity<>(entertainmentDtos, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntertainmentResponseDTO> getEntertainmentById(@PathVariable UUID id) {
        Optional<Entertainment> entertainment = entertainmentService.getEntertainmentById(id);
        return entertainment.map(e -> new ResponseEntity<>(convertToEntertainmentResponseDTO(e), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<EntertainmentResponseDTO> createEntertainment(@RequestBody EntertainmentCreateDTO entertainmentCreateDTO) {
        Entertainment entertainment = convertToEntertainment(entertainmentCreateDTO);
        Entertainment createdEntertainment = entertainmentService.createEntertainment(entertainment);
        return new ResponseEntity<>(convertToEntertainmentResponseDTO(createdEntertainment), HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<EntertainmentResponseDTO> updateEntertainment(@PathVariable UUID id,
            @RequestBody EntertainmentUpdateDTO entertainmentUpdateDTO) {
        Entertainment entertainment = convertToEntertainment(entertainmentUpdateDTO);
        entertainment.setId(id);
        Entertainment updatedEntertainment = entertainmentService.updateEntertainment(id, entertainment);
        return updatedEntertainment != null ?
                new ResponseEntity<>(convertToEntertainmentResponseDTO(updatedEntertainment), HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEntertainment(@PathVariable UUID id) {
        return entertainmentService.deleteEntertainment(id) ?
                new ResponseEntity<>(HttpStatus.NO_CONTENT) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    private Entertainment convertToEntertainment(EntertainmentCreateDTO entertainmentCreateDTO) {
        Entertainment entertainment = new Entertainment();
        entertainment.setTitle(entertainmentCreateDTO.getTitle());
        entertainment.setGenre(entertainmentCreateDTO.getGenre());
        entertainment.setDescription(entertainmentCreateDTO.getDescription());
        return entertainment;
    }

    private Entertainment convertToEntertainment(EntertainmentUpdateDTO entertainmentUpdateDTO) {
        Entertainment entertainment = new Entertainment();
        entertainment.setTitle(entertainmentUpdateDTO.getTitle());
        entertainment.setGenre(entertainmentUpdateDTO.getGenre());
        entertainment.setDescription(entertainmentUpdateDTO.getDescription());
        return entertainment;
    }

    private EntertainmentResponseDTO convertToEntertainmentResponseDTO(Entertainment entertainment) {
        EntertainmentResponseDTO entertainmentResponseDTO = new EntertainmentResponseDTO();
        entertainmentResponseDTO.setId(entertainment.getId());
        entertainmentResponseDTO.setTitle(entertainment.getTitle());
        entertainmentResponseDTO.setGenre(entertainment.getGenre());
        entertainmentResponseDTO.setDescription(entertainment.getDescription());
        return entertainmentResponseDTO;
    }
}