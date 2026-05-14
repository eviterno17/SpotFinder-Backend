package com.spotfinderbackend.payments.domain.services;

import com.spotfinderbackend.payments.domain.model.valueobjects.ParkingFee;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;

/**
 * Domain service that computes parking fees.
 * Rule (PDF §4.2.3): amount = ceil(hours) × ratePerHour. Default rate = S/ 5.00.
 */
@Service
public class FeeCalculationService {

    private final BigDecimal defaultRatePerHour;

    public FeeCalculationService(@Value("${spotfinder.parking.rate-per-hour:5.00}") BigDecimal defaultRatePerHour) {
        this.defaultRatePerHour = defaultRatePerHour;
    }

    public ParkingFee calculateFee(LocalDateTime entryTimestamp, LocalDateTime currentTimestamp,
                                   BigDecimal ratePerHour) {
        if (entryTimestamp == null) entryTimestamp = LocalDateTime.now();
        if (currentTimestamp == null) currentTimestamp = LocalDateTime.now();
        if (ratePerHour == null) ratePerHour = defaultRatePerHour;

        Duration duration = Duration.between(entryTimestamp, currentTimestamp);
        if (duration.isNegative()) duration = Duration.ZERO;

        long minutes = Math.max(duration.toMinutes(), 1L);
        int hours = (int) Math.ceil(minutes / 60.0);
        BigDecimal amount = ratePerHour.multiply(BigDecimal.valueOf(hours))
                .setScale(2, RoundingMode.HALF_UP);

        return new ParkingFee(amount, duration.getSeconds(),
                ratePerHour.setScale(2, RoundingMode.HALF_UP), hours);
    }

    public BigDecimal getDefaultRatePerHour() {
        return defaultRatePerHour;
    }
}
