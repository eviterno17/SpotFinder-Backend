package com.spotfinderbackend.emergency.infrastructure.persistence.jpa.repositories;

import com.spotfinderbackend.emergency.domain.model.aggregates.EmergencyAlert;
import com.spotfinderbackend.emergency.domain.model.valueobjects.EmergencyStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface EmergencyAlertRepository extends JpaRepository<EmergencyAlert, Long> {

    List<EmergencyAlert> findByStatus(EmergencyStatus status);

    List<EmergencyAlert> findByTriggeredAtBetween(LocalDateTime start, LocalDateTime end);

    boolean existsByStatus(EmergencyStatus status);

    Optional<EmergencyAlert> findFirstByStatusOrderByTriggeredAtDesc(EmergencyStatus status);
}
