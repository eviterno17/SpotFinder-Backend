package com.spotfinderbackend.analytics.domain.model.queries;

import java.time.LocalDate;

public record GetHeatmapDataQuery(LocalDate startDate, LocalDate endDate, Long facilityId) { }
