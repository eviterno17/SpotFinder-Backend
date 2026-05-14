package com.spotfinderbackend.parkingmonitoring.infrastructure.persistence.jpa.repositories;

import com.spotfinderbackend.parkingmonitoring.domain.model.aggregates.ParkingFacility;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParkingFacilityRepository extends JpaRepository<ParkingFacility, Long> {
}
