package com.spotfinderbackend.parkingmonitoring.domain.model.commands;

import java.time.LocalDateTime;

public record ProcessSensorReadingCommand(String sensorId, Long slotId, double distance, LocalDateTime timestamp) {
    public ProcessSensorReadingCommand {
        if (sensorId == null || sensorId.isBlank())
            throw new IllegalArgumentException("sensorId is required");
        if (distance < 0)
            throw new IllegalArgumentException("distance must be non-negative");
        if (timestamp == null) timestamp = LocalDateTime.now();
    }
}
