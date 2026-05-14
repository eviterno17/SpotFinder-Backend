package com.spotfinderbackend.parkingmonitoring.interfaces.rest.resources;

import java.time.LocalDateTime;

public record SensorReadingResource(String sensorId, Long slotId, double distance, LocalDateTime timestamp) { }
