package com.spotfinderbackend.parkingmonitoring.infrastructure.persistence.jpa.repositories;

import com.spotfinderbackend.parkingmonitoring.domain.model.aggregates.SensorReading;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SensorReadingRepository extends JpaRepository<SensorReading, Long> {

    List<SensorReading> findBySensorIdAndReadingTimestampBetween(String sensorId,
                                                                  LocalDateTime start,
                                                                  LocalDateTime end);
}
