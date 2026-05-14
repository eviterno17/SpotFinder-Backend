package com.spotfinderbackend.analytics.interfaces.rest.resources;

import java.util.List;
import java.util.Map;

public record OccupancyMetricsResource(
        double averageOccupancyRate,
        List<Integer> peakHours,
        double turnoverRate,
        Map<Integer, Double> dataByHour,
        int totalSlots,
        String periodStart,
        String periodEnd
) { }
