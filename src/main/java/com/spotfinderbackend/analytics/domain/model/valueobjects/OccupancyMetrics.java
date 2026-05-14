package com.spotfinderbackend.analytics.domain.model.valueobjects;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public record OccupancyMetrics(
        double averageOccupancyRate,
        List<Integer> peakHours,
        double turnoverRate,
        Map<Integer, Double> dataByHour,
        int totalSlots,
        LocalDate periodStart,
        LocalDate periodEnd
) { }
