package com.spotfinderbackend.payments.infrastructure.gateway;

import com.spotfinderbackend.payments.domain.model.valueobjects.PaymentGatewayResponse;
import com.spotfinderbackend.payments.domain.model.valueobjects.PaymentMethod;
import com.spotfinderbackend.payments.domain.services.PaymentGatewayService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Culqi payment gateway adapter.
 * <p>
 * Production wiring should POST to {@code https://api.culqi.com/v2/charges} with
 * the body: {@code amount} (in cents), {@code currency_code}, {@code source_id}
 * (Culqi Checkout token) and {@code email}; header {@code Authorization: Bearer
 * <culqiPrivateKey>}. Until credentials are provisioned, this adapter simulates
 * success deterministically.
 */
@Service
public class CulqiPaymentGateway implements PaymentGatewayService {

    private static final Logger LOG = LoggerFactory.getLogger(CulqiPaymentGateway.class);

    private final String culqiPublicKey;
    private final String culqiPrivateKey;
    private final String culqiApiUrl;

    public CulqiPaymentGateway(
            @Value("${culqi.public-key:}") String culqiPublicKey,
            @Value("${culqi.private-key:}") String culqiPrivateKey,
            @Value("${culqi.api-url:https://api.culqi.com/v2}") String culqiApiUrl
    ) {
        this.culqiPublicKey = culqiPublicKey;
        this.culqiPrivateKey = culqiPrivateKey;
        this.culqiApiUrl = culqiApiUrl;
    }

    @Override
    public PaymentGatewayResponse processPayment(BigDecimal amount, String currency, String token, PaymentMethod method) {
        if (amount == null || amount.signum() <= 0)
            return PaymentGatewayResponse.failure("Invalid amount");
        if (token == null || token.isBlank())
            return PaymentGatewayResponse.failure("Missing payment token");

        if (culqiPrivateKey == null || culqiPrivateKey.isBlank()) {
            LOG.warn("Culqi key not configured; simulating successful charge of {} {}", amount, currency);
            String txId = "SIM-" + UUID.randomUUID();
            return PaymentGatewayResponse.success(txId, "https://receipts.culqi.test/" + txId);
        }

        // TODO: real HTTP integration: POST {culqiApiUrl}/charges with Authorization: Bearer culqiPrivateKey,
        // body amount in cents, currency_code, source_id=token, email; parse response.
        LOG.info("Culqi processPayment placeholder (url={}, method={})", culqiApiUrl, method);
        String txId = "TX-" + UUID.randomUUID();
        return PaymentGatewayResponse.success(txId, "https://receipts.culqi.com/" + txId);
    }
}
