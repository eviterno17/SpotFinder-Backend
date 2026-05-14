package com.spotfinderbackend.wallet.interfaces.rest.resources;

import java.time.LocalDateTime;

public record DigitalPassResource(
        Long id,
        Long sessionId,
        String qrCode,
        String applePayload,
        String googlePayload,
        LocalDateTime createdAt
) {}