package com.spotfinderbackend.analytics.domain.services;

import com.spotfinderbackend.analytics.domain.model.queries.*;
import com.spotfinderbackend.analytics.domain.model.valueobjects.*;

import java.util.List;

public interface AnalyticsQueryService {
    OccupancyMetrics handle(GetOccupancyMetricsQuery query);
    RevenueMetrics handle(GetRevenueMetricsQuery query);
    List<HeatmapEntry> handle(GetHeatmapDataQuery query);
    PeakHoursData handle(GetPeakHoursQuery query);
}
