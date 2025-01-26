package com.popov.app.repository;

import com.popov.app.model.Entertainment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface EntertainmentRepository extends JpaRepository<Entertainment, UUID> {
    
}
