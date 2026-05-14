package com.spotfinderbackend.analytics.domain.services;

import com.spotfinderbackend.analytics.domain.model.valueobjects.PaymentSummary;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RevenueAnalyticsService {

    public BigDecimal calculateTotalRevenue(List<PaymentSummary> payments) {
        if (payments == null || payments.isEmpty()) return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        return payments.stream().map(p -> p.amount() == null ? BigDecimal.ZERO : p.amount())
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal calculateAverageTicket(List<PaymentSummary> payments) {
        if (payments == null || payments.isEmpty()) return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        return calculateTotalRevenue(payments).divide(BigDecimal.valueOf(payments.size()), 2, RoundingMode.HALF_UP);
    }

    public Map<String, BigDecimal> groupByPaymentMethod(List<PaymentSummary> payments) {
        Map<String, BigDecimal> result = new HashMap<>();
        if (payments == null) return result;
        for (var p : payments) {
            String key = p.paymentMethod() == null ? "UNKNOWN" : p.paymentMethod();
            BigDecimal amount = p.amount() == null ? BigDecimal.ZERO : p.amount();
            result.merge(key, amount, BigDecimal::add);
        }
        return result;
    }

    public Map<LocalDate, BigDecimal> groupByDay(List<PaymentSummary> payments) {
        Map<LocalDate, BigDecimal> result = new HashMap<>();
        if (payments == null) return result;
        for (var p : payments) {
            if (p.paidAt() == null) continue;
            LocalDate day = p.paidAt().toLocalDate();
            BigDecimal amount = p.amount() == null ? BigDecimal.ZERO : p.amount();
            result.merge(day, amount, BigDecimal::add);
        }
        return result;
    }
}
