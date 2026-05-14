package com.spotfinderbackend.analytics.interfaces.rest.transform;

import com.spotfinderbackend.analytics.domain.model.aggregates.Report;
import com.spotfinderbackend.analytics.domain.model.commands.GenerateReportCommand;
import com.spotfinderbackend.analytics.domain.model.valueobjects.*;
import com.spotfinderbackend.analytics.interfaces.rest.resources.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnalyticsResourceAssembler {

    public static OccupancyMetricsResource toResource(OccupancyMetrics m) {
        return new OccupancyMetricsResource(
                m.averageOccupancyRate(), m.peakHours(), m.turnoverRate(),
                m.dataByHour(), m.totalSlots(),
                m.periodStart() == null ? null : m.periodStart().toString(),
                m.periodEnd() == null ? null : m.periodEnd().toString()
        );
    }

    public static RevenueMetricsResource toResource(RevenueMetrics m) {
        Map<String, BigDecimal> byDay = new HashMap<>();
        m.dataByDay().forEach((k, v) -> byDay.put(k.toString(), v));
        return new RevenueMetricsResource(m.totalRevenue(), m.averageTicket(),
                m.totalTransactions(), m.paymentsByMethod(), byDay, m.currency());
    }

    public static HeatmapEntryResource toResource(HeatmapEntry e) {
        return new HeatmapEntryResource(e.slotId(), e.slotCode(), e.usageCount(), e.averageDurationMinutes());
    }

    public static List<HeatmapEntryResource> toHeatmapResources(Collection<HeatmapEntry> entries) {
        return entries.stream().map(AnalyticsResourceAssembler::toResource).toList();
    }

    public static PeakHoursResource toResource(PeakHoursData p) {
        return new PeakHoursResource(p.peakHours(), p.occupancyByHour(), p.busiestDay());
    }

    public static ReportResource toResource(Report r) {
        return new ReportResource(
                r.getId(), r.getReportType().name(),
                r.getReportPeriod() == null ? null : r.getReportPeriod().startDate().toString(),
                r.getReportPeriod() == null ? null : r.getReportPeriod().endDate().toString(),
                r.getGeneratedAt(), r.getStatus().name(), r.getFileUrl(), r.getFacilityId()
        );
    }

    public static List<ReportResource> toReportResources(Collection<Report> reports) {
        return reports.stream().map(AnalyticsResourceAssembler::toResource).toList();
    }

    public static GenerateReportCommand toCommand(GenerateReportResource r) {
        return new GenerateReportCommand(
                ReportType.valueOf(r.reportType().toUpperCase()),
                LocalDate.parse(r.startDate()), LocalDate.parse(r.endDate()),
                r.generatedBy(), r.facilityId()
        );
    }
}
