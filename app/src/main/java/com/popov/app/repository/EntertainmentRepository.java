package com.popov.app.repository;

import com.popov.app.model.Entertainment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface EntertainmentRepository extends JpaRepository<Entertainment, UUID> {
}
