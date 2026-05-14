package com.spotfinderbackend.emergency.domain.model.commands;

public record ResolveEmergencyCommand(Long emergencyId, Long adminUserId) {
    public ResolveEmergencyCommand {
        if (emergencyId == null || emergencyId <= 0)
            throw new IllegalArgumentException("emergencyId must be > 0");
    }
}
