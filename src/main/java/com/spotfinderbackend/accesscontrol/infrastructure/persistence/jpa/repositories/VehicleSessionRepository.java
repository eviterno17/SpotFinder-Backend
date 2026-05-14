package com.spotfinderbackend.accesscontrol.infrastructure.persistence.jpa.repositories;

import com.spotfinderbackend.accesscontrol.domain.model.aggregates.VehicleSession;
import com.spotfinderbackend.accesscontrol.domain.model.valueobjects.SessionStatus;
import com.spotfinderbackend.accesscontrol.domain.model.valueobjects.UserId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VehicleSessionRepository extends JpaRepository<VehicleSession, Long> {

    Optional<VehicleSession> findByLicensePlate_PlateTextAndSessionStatus(String plateText, SessionStatus status);

    Optional<VehicleSession> findByUserIdAndSessionStatus(UserId userId, SessionStatus status);

    List<VehicleSession> findByUserIdOrderByEntryTimestampDesc(UserId userId);

    boolean existsByLicensePlate_PlateTextAndSessionStatus(String plateText, SessionStatus status);
}
