package com.spotfinderbackend.parkingmonitoring.domain.model.commands;

public record RegisterParkingSlotCommand(String slotCode, String sensorId, Long facilityId) {
    public RegisterParkingSlotCommand {
        if (slotCode == null || slotCode.isBlank())
            throw new IllegalArgumentException("slotCode is required");
        if (facilityId == null || facilityId <= 0)
            throw new IllegalArgumentException("facilityId must be > 0");
    }
}
