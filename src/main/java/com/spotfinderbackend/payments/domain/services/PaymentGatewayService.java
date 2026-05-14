package com.spotfinderbackend.payments.domain.services;

import com.spotfinderbackend.payments.domain.model.valueobjects.PaymentGatewayResponse;
import com.spotfinderbackend.payments.domain.model.valueobjects.PaymentMethod;

import java.math.BigDecimal;

/**
 * Outbound port to a payment gateway (e.g. Culqi). Implemented in infrastructure.
 */
public interface PaymentGatewayService {
    PaymentGatewayResponse processPayment(BigDecimal amount, String currency, String token, PaymentMethod method);
}
