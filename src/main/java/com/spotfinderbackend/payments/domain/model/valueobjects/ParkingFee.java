package com.spotfinderbackend.payments.domain.model.valueobjects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;

@Embeddable
public record ParkingFee(
        @Column(name = "fee_amount", precision = 15, scale = 2) BigDecimal amount,
        @Column(name = "fee_duration_seconds") Long durationSeconds,
        @Column(name = "fee_rate_per_hour", precision = 10, scale = 2) BigDecimal ratePerHour,
        @Column(name = "fee_hours_charged") Integer hoursCharged
) {
    public ParkingFee() { this(BigDecimal.ZERO, 0L, BigDecimal.ZERO, 0); }

    public ParkingFee {
        if (amount == null) amount = BigDecimal.ZERO;
        if (ratePerHour == null) ratePerHour = BigDecimal.ZERO;
        if (hoursCharged == null) hoursCharged = 0;
        if (durationSeconds == null) durationSeconds = 0L;
        amount = amount.setScale(2, RoundingMode.HALF_UP);
        ratePerHour = ratePerHour.setScale(2, RoundingMode.HALF_UP);
    }

    public Duration duration() { return Duration.ofSeconds(durationSeconds); }

    public String getFormattedDuration() {
        var d = duration();
        return d.toHours() + "h " + d.toMinutesPart() + "min";
    }
}
