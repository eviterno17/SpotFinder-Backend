package com.spotfinderbackend.payments.interfaces.rest.resources;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PaymentResource(
        Long id,
        Long sessionId,
        BigDecimal amount,
        String currency,
        String paymentMethod,
        String status,
        String transactionId,
        String receiptUrl,
        LocalDateTime paidAt,
        String duration,
        int hoursCharged
) { }
