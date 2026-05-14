package com.spotfinderbackend.emergency.domain.model.valueobjects;

import com.spotfinderbackend.shared.domain.model.exceptions.BadRequestException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public record EmergencySensorId(@Column(name = "sensor_id", length = 50, nullable = false) String value) {
    public EmergencySensorId() { this("UNKNOWN"); }
    public EmergencySensorId {
        if (value == null || value.isBlank())
            throw new BadRequestException("EmergencySensorId is required");
    }
}
