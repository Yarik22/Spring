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

    public List<Entertainment> getAllEntertainments() {
        return entertainmentRepository.findAll();
    }

    public Optional<Entertainment> getEntertainmentById(UUID id) {
        return entertainmentRepository.findById(id);
    }

    public Entertainment createEntertainment(Entertainment entertainment) {
        return entertainmentRepository.save(entertainment);
    }

    public Entertainment updateEntertainment(UUID id, Entertainment entertainment) {
        if (entertainmentRepository.existsById(id)) {
            entertainment.setId(id);
            return entertainmentRepository.save(entertainment);
        }
        return null;
    }

    public boolean deleteEntertainment(UUID id) {
        if (entertainmentRepository.existsById(id)) {
            entertainmentRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
