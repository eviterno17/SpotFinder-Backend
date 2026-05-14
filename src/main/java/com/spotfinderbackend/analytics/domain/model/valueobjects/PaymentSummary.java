package com.spotfinderbackend.analytics.domain.model.valueobjects;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PaymentSummary(Long paymentId, BigDecimal amount, String paymentMethod, LocalDateTime paidAt) { }
