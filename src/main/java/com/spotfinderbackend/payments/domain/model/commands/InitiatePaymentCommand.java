package com.spotfinderbackend.payments.domain.model.commands;

import com.spotfinderbackend.payments.domain.model.valueobjects.PaymentMethod;

public record InitiatePaymentCommand(Long sessionId, PaymentMethod paymentMethod, String token) {
    public InitiatePaymentCommand {
        if (sessionId == null || sessionId <= 0)
            throw new IllegalArgumentException("sessionId must be > 0");
        if (paymentMethod == null)
            throw new IllegalArgumentException("paymentMethod is required");
        if (token == null || token.isBlank())
            throw new IllegalArgumentException("token is required");
    }
}
