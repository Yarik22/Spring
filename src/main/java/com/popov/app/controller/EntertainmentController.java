package com.popov.app.controller;

import com.popov.app.dto.entartainment.EntertainmentCreateDTO;
import com.popov.app.dto.entartainment.EntertainmentResponseDTO;
import com.popov.app.dto.entartainment.EntertainmentUpdateDTO;
import com.popov.app.model.Entertainment;
import com.popov.app.service.EntertainmentService;
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
@RequestMapping("/api/entertainment")
public class EntertainmentController {

        
    @Autowired
    private final EntertainmentService entertainmentService;

    public EntertainmentController(EntertainmentService entertainmentService) {
        this.entertainmentService = entertainmentService;
    }

    @Operation(summary = "Get all entertainment items", description = "Retrieve a list of all entertainment items")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of entertainment items"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public ResponseEntity<List<EntertainmentResponseDTO>> getAllEntertainment() {
        List<Entertainment> entertainmentItems = entertainmentService.getAllEntertainments();
        List<EntertainmentResponseDTO> entertainmentDtos = entertainmentItems.stream()
                .map(this::convertToEntertainmentResponseDTO)
                .collect(Collectors.toList());
        return new ResponseEntity<>(entertainmentDtos, HttpStatus.OK);
    }

    @Operation(summary = "Get an entertainment item by ID", description = "Retrieve a specific entertainment item by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved entertainment item"),
            @ApiResponse(responseCode = "404", description = "Entertainment item not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{id}")
    public ResponseEntity<EntertainmentResponseDTO> getEntertainmentById(
            @Parameter(description = "ID of the entertainment item to be retrieved") @PathVariable UUID id) {
        Optional<Entertainment> entertainment = entertainmentService.getEntertainmentById(id);
        return entertainment.map(e -> new ResponseEntity<>(convertToEntertainmentResponseDTO(e), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Operation(summary = "Create a new entertainment item", description = "Create a new entertainment item with provided details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Entertainment item successfully created"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public ResponseEntity<EntertainmentResponseDTO> createEntertainment(@RequestBody EntertainmentCreateDTO entertainmentCreateDTO) {
        Entertainment entertainment = convertToEntertainment(entertainmentCreateDTO);
        Entertainment createdEntertainment = entertainmentService.createEntertainment(entertainment);
        return new ResponseEntity<>(convertToEntertainmentResponseDTO(createdEntertainment), HttpStatus.CREATED);
    }

    @Operation(summary = "Update an entertainment item", description = "Update an existing entertainment item by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Entertainment item successfully updated"),
            @ApiResponse(responseCode = "404", description = "Entertainment item not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PatchMapping("/{id}")
    public ResponseEntity<EntertainmentResponseDTO> updateEntertainment(
            @Parameter(description = "ID of the entertainment item to be updated") @PathVariable UUID id,
            @RequestBody EntertainmentUpdateDTO entertainmentUpdateDTO) {
        Entertainment entertainment = convertToEntertainment(entertainmentUpdateDTO);
        entertainment.setId(id);
        Entertainment updatedEntertainment = entertainmentService.updateEntertainment(id, entertainment);
        return updatedEntertainment != null ?
                new ResponseEntity<>(convertToEntertainmentResponseDTO(updatedEntertainment), HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @Operation(summary = "Delete an entertainment item", description = "Delete an entertainment item by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Entertainment item successfully deleted"),
            @ApiResponse(responseCode = "404", description = "Entertainment item not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEntertainment(
            @Parameter(description = "ID of the entertainment item to be deleted") @PathVariable UUID id) {
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