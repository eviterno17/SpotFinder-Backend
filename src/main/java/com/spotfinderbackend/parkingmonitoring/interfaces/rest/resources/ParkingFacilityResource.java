package com.spotfinderbackend.parkingmonitoring.interfaces.rest.resources;

public record ParkingFacilityResource(Long id, String name, int totalSlots, String address) { }
