package com.spotfinderbackend.payments.interfaces.rest.resources;

public record InitiatePaymentResource(Long sessionId, String paymentMethod, String token) { }
