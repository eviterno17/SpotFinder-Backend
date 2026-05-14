package com.spotfinderbackend.analytics.domain.model.queries;

import java.time.LocalDate;

public record GetOccupancyMetricsQuery(LocalDate startDate, LocalDate endDate, Long facilityId) { }
