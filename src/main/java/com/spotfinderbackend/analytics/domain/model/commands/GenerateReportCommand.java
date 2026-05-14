package com.spotfinderbackend.analytics.domain.model.commands;

import com.spotfinderbackend.analytics.domain.model.valueobjects.ReportType;

import java.time.LocalDate;

public record GenerateReportCommand(ReportType reportType, LocalDate startDate, LocalDate endDate,
                                    Long generatedBy, Long facilityId) {
    public GenerateReportCommand {
        if (reportType == null)
            throw new IllegalArgumentException("reportType is required");
        if (startDate == null || endDate == null)
            throw new IllegalArgumentException("startDate and endDate are required");
        if (startDate.isAfter(endDate))
            throw new IllegalArgumentException("startDate must be <= endDate");
    }
}
