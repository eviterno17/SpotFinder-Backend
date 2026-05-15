package com.spotfinderbackend.iam.domain.model.commands;

public record RegisterVehicleCommand(Long userId, String plate, String brand, String model, String color) {
    public RegisterVehicleCommand {
        if (userId == null || userId <= 0)
            throw new IllegalArgumentException("userId is required");
        if (plate == null || plate.isBlank())
            throw new IllegalArgumentException("plate is required");
    }
}
