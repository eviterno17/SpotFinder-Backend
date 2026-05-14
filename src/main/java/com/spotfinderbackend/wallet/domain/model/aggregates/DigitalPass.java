package com.spotfinderbackend.wallet.domain.model.aggregates;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Entity
public class DigitalPass {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long sessionId;

    private String qrCode;

    private String applePayload;

    private String googlePayload;

    private LocalDateTime createdAt;

    protected DigitalPass() {}

    public DigitalPass(Long sessionId) {
        this.sessionId = sessionId;
        this.createdAt = LocalDateTime.now();

        // 🔥 generación simulada
        this.qrCode = "QR-" + sessionId + "-" + System.currentTimeMillis();
        this.applePayload = "{}";
        this.googlePayload = "{}";
    }

    public void generateApplePass() {
        this.applePayload =
                "{ \"passType\": \"APPLE\", \"sessionId\": " + sessionId + " }";
    }

    public void generateGooglePass() {
        this.googlePayload =
                "{ \"passType\": \"GOOGLE\", \"sessionId\": " + sessionId + " }";
    }
}