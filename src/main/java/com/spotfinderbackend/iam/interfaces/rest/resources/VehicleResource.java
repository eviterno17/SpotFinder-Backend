package com.spotfinderbackend.iam.interfaces.rest.resources;

import java.time.LocalDateTime;

public record VehicleResource(Long id, Long userId, String plate, String brand,
                              String model, String color, LocalDateTime createdAt) { }
