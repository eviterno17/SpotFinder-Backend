package com.spotfinderbackend.analytics.domain.model.valueobjects;

public record HeatmapEntry(Long slotId, String slotCode, int usageCount,
                           double averageDurationMinutes, long totalOccupiedMinutes) { }
