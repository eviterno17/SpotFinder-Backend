package com.spotfinderbackend.analytics.domain.model.valueobjects;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

public record RevenueMetrics(
        BigDecimal totalRevenue,
        BigDecimal averageTicket,
        int totalTransactions,
        Map<String, BigDecimal> paymentsByMethod,
        Map<LocalDate, BigDecimal> dataByDay,
        String currency
) { }
