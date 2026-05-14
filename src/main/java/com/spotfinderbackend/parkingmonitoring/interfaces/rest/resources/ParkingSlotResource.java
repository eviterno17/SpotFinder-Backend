package com.spotfinderbackend.parkingmonitoring.interfaces.rest.resources;

import java.time.LocalDateTime;

public record ParkingSlotResource(
        Long id,
        String slotCode,
        String status,
        String sensorId,
        Long facilityId,
        LocalDateTime lastUpdated
) { }
