package com.spotfinderbackend.parkingmonitoring.domain.model.commands;

public record RegisterFacilityCommand(String name, int totalSlots, String address) {
    public RegisterFacilityCommand {
        if (name == null || name.isBlank())
            throw new IllegalArgumentException("name is required");
        if (totalSlots < 0)
            throw new IllegalArgumentException("totalSlots must be non-negative");
    }
}
