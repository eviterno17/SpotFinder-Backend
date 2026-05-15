package com.spotfinderbackend.iam.domain.model.commands;

public record DeleteVehicleCommand(Long vehicleId, Long userId) {
    public DeleteVehicleCommand {
        if (vehicleId == null || vehicleId <= 0)
            throw new IllegalArgumentException("vehicleId is required");
        if (userId == null || userId <= 0)
            throw new IllegalArgumentException("userId is required");
    }
}
