package com.spotfinderbackend.parkingmonitoring.domain.model.aggregates;

import com.spotfinderbackend.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * Historical record of a single sensor reading. Used to compute analytics and
 * for traceability of slot status changes.
 */
@Entity
@Getter
public class SensorReading extends AuditableAbstractAggregateRoot<SensorReading> {

    @Column(nullable = false, length = 50)
    private String sensorId;

    @Column
    private Long slotId;

    @Column(nullable = false)
    private double distance;

    @Column(nullable = false)
    private LocalDateTime readingTimestamp;

    protected SensorReading() {}

    public SensorReading(String sensorId, Long slotId, double distance, LocalDateTime readingTimestamp) {
        this.sensorId = sensorId;
        this.slotId = slotId;
        this.distance = distance;
        this.readingTimestamp = readingTimestamp == null ? LocalDateTime.now() : readingTimestamp;
    }
}
