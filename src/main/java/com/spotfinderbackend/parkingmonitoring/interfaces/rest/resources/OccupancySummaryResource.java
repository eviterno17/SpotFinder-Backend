package com.spotfinderbackend.parkingmonitoring.interfaces.rest.resources;

public record OccupancySummaryResource(long total, long available, long occupied, double occupancyRate) { }
