package com.spotfinderbackend.analytics.domain.services;

import com.spotfinderbackend.analytics.domain.model.valueobjects.OccupancyMetrics;
import com.spotfinderbackend.analytics.domain.model.valueobjects.ReportPeriod;
import com.spotfinderbackend.analytics.domain.model.valueobjects.RevenueMetrics;

public interface PdfGenerationService {
    byte[] generateOccupancyReport(OccupancyMetrics metrics, ReportPeriod period);
    byte[] generateRevenueReport(RevenueMetrics metrics, ReportPeriod period);
    byte[] generateCombinedReport(OccupancyMetrics occupancy, RevenueMetrics revenue, ReportPeriod period);
}
