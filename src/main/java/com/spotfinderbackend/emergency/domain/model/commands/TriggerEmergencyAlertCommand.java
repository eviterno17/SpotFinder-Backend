package com.spotfinderbackend.emergency.domain.model.commands;

import com.spotfinderbackend.emergency.domain.model.valueobjects.EmergencyType;

public record TriggerEmergencyAlertCommand(String sensorId, int gasLevel, EmergencyType type, String sensorLocation) {
    public TriggerEmergencyAlertCommand {
        if (sensorId == null || sensorId.isBlank())
            throw new IllegalArgumentException("sensorId is required");
        if (gasLevel < 0)
            throw new IllegalArgumentException("gasLevel must be non-negative");
        if (type == null)
            throw new IllegalArgumentException("type is required");
    }
}
