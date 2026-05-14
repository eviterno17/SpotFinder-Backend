package com.spotfinderbackend.analytics.domain.model.valueobjects;

import com.spotfinderbackend.shared.domain.model.exceptions.BadRequestException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Embeddable
public record ReportPeriod(
        @Column(name = "period_start") LocalDate startDate,
        @Column(name = "period_end") LocalDate endDate
) {
    public ReportPeriod() { this(LocalDate.now(), LocalDate.now()); }

    public ReportPeriod {
        if (startDate == null || endDate == null)
            throw new BadRequestException("Period dates are required");
        if (startDate.isAfter(endDate))
            throw new BadRequestException("startDate must be before endDate");
    }

    public long getDurationInDays() {
        return ChronoUnit.DAYS.between(startDate, endDate) + 1;
    }

    public boolean includes(LocalDate date) {
        return date != null && !date.isBefore(startDate) && !date.isAfter(endDate);
    }
}
