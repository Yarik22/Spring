package com.popov.app.service;

import com.popov.app.model.Entertainment;
import com.popov.app.repository.EntertainmentRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class EntertainmentService {
    
    private final EntertainmentRepository entertainmentRepository;

    public EntertainmentService(EntertainmentRepository entertainmentRepository) {
        this.entertainmentRepository = entertainmentRepository;
    }

    public List<Entertainment> getAllEntertainment() {
        return entertainmentRepository.findAll();
    }

    public Optional<Entertainment> getEntertainmentById(UUID id) {
        return entertainmentRepository.findById(id);
    }

    public Entertainment saveEntertainment(Entertainment entertainment) {
        return entertainmentRepository.save(entertainment);
    }

    public Entertainment updateEntertainment(UUID id, Entertainment entertainmentDetails) {
        return entertainmentRepository.findById(id).map(entertainment -> {
            entertainment.setTitle(entertainmentDetails.getTitle());
            entertainment.setGenre(entertainmentDetails.getGenre());
            entertainment.setDescription(entertainmentDetails.getDescription());
            entertainment.setDuration(entertainmentDetails.getDuration());
            entertainment.setRating(entertainmentDetails.getRating());
            return entertainmentRepository.save(entertainment);
        }).orElseThrow(() -> new RuntimeException("Entertainment not found"));
    }

    public void deleteEntertainment(UUID id) {
        entertainmentRepository.deleteById(id);
    }
}
