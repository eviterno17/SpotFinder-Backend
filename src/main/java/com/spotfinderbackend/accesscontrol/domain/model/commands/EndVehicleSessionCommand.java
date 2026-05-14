package com.spotfinderbackend.accesscontrol.domain.model.commands;

import java.time.LocalDateTime;

public record EndVehicleSessionCommand(Long sessionId, LocalDateTime exitTimestamp) {
    public EndVehicleSessionCommand {
        if (sessionId == null || sessionId <= 0)
            throw new IllegalArgumentException("sessionId must be > 0");
        if (exitTimestamp == null) exitTimestamp = LocalDateTime.now();
    }
}
