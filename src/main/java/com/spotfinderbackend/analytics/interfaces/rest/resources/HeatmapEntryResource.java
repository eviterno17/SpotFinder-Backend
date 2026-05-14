package com.spotfinderbackend.analytics.interfaces.rest.resources;

public record HeatmapEntryResource(Long slotId, String slotCode, int usageCount, double averageDurationMinutes) { }
