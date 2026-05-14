package com.spotfinderbackend.accesscontrol.interfaces.rest.resources;

import java.time.LocalDateTime;

public record VehicleSessionResource(
        Long id,
        String licensePlate,
        LocalDateTime entryTimestamp,
        LocalDateTime exitTimestamp,
        Long slotId,
        String paymentStatus,
        String sessionStatus,
        String currentDuration,
        Long userId
) { }
