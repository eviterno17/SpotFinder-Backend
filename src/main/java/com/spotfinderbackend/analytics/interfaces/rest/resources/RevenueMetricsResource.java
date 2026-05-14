package com.spotfinderbackend.analytics.interfaces.rest.resources;

import java.math.BigDecimal;
import java.util.Map;

public record RevenueMetricsResource(
        BigDecimal totalRevenue,
        BigDecimal averageTicket,
        int totalTransactions,
        Map<String, BigDecimal> paymentsByMethod,
        Map<String, BigDecimal> dataByDay,
        String currency
) { }
