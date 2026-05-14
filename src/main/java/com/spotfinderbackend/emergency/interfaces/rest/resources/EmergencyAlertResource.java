package com.spotfinderbackend.emergency.interfaces.rest.resources;

import java.time.LocalDateTime;

public record EmergencyAlertResource(
        Long id, String sensorId, int gasLevel, String type, String status,
        LocalDateTime triggeredAt, LocalDateTime resolvedAt, Long resolvedBy, String sensorLocation
) { }
