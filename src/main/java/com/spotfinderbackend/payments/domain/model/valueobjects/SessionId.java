package com.spotfinderbackend.payments.domain.model.valueobjects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public record SessionId(@Column(name = "session_id", nullable = false) Long value) {
    public SessionId() { this(0L); }
    public SessionId {
        if (value == null || value <= 0)
            throw new IllegalArgumentException("SessionId must be > 0");
    }
}
