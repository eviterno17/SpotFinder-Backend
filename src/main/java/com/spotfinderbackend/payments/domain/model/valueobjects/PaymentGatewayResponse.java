package com.spotfinderbackend.payments.domain.model.valueobjects;

/** Response returned by an external payment gateway (e.g. Culqi). */
public record PaymentGatewayResponse(boolean success, String transactionId, String errorDetail, String receiptUrl) {
    public static PaymentGatewayResponse success(String transactionId, String receiptUrl) {
        return new PaymentGatewayResponse(true, transactionId, null, receiptUrl);
    }
    public static PaymentGatewayResponse failure(String errorDetail) {
        return new PaymentGatewayResponse(false, null, errorDetail, null);
    }
}
