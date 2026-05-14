package com.spotfinderbackend.emergency.interfaces.rest.resources;

import java.time.LocalDateTime;

public record EmergencyStatusResource(
        boolean isEmergencyActive, Long emergencyId, String type, int gasLevel,
        String sensorLocation, LocalDateTime triggeredAt, String overallStatus
) { }
