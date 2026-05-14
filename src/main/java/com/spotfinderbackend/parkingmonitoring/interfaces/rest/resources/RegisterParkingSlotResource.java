package com.spotfinderbackend.parkingmonitoring.interfaces.rest.resources;

public record RegisterParkingSlotResource(String slotCode, String sensorId, Long facilityId) { }
