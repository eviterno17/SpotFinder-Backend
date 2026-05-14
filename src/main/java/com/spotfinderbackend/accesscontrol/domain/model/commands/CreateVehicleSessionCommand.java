package com.spotfinderbackend.accesscontrol.domain.model.commands;

import java.time.LocalDateTime;

public record CreateVehicleSessionCommand(String licensePlate, LocalDateTime entryTimestamp, Long userId) {
    public CreateVehicleSessionCommand {
        if (licensePlate == null || licensePlate.isBlank())
            throw new IllegalArgumentException("licensePlate is required");
        if (entryTimestamp == null) entryTimestamp = LocalDateTime.now();
    }
}
