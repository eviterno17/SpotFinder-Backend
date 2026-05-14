package com.spotfinderbackend.analytics.domain.model.queries;

import java.time.LocalDate;

public record GetRevenueMetricsQuery(LocalDate startDate, LocalDate endDate, Long facilityId) { }
