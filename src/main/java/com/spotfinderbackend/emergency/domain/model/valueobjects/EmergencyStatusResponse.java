package com.spotfinderbackend.emergency.domain.model.valueobjects;

import java.time.LocalDateTime;

public record EmergencyStatusResponse(
        boolean isEmergencyActive,
        Long emergencyId,
        EmergencyType type,
        int gasLevel,
        String sensorLocation,
        LocalDateTime triggeredAt,
        String overallStatus
) {
    public static EmergencyStatusResponse normal() {
        return new EmergencyStatusResponse(false, null, null, 0, null, null, "NORMAL");
    }
}
