package com.spotfinderbackend.parkingmonitoring.domain.model.valueobjects;

import com.spotfinderbackend.shared.domain.model.exceptions.BadRequestException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public record SensorId(@Column(name = "sensor_id", length = 50) String value) {
    public SensorId() { this(null); }

    public SensorId {
        if (value != null && value.isBlank())
            throw new BadRequestException("SensorId must not be blank");
    }
}
