package com.spotfinderbackend.notifications.domain.model.valueobjects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public record NotificationUserId(@Column(name = "user_id", nullable = false) Long value) {
    public NotificationUserId() { this(0L); }
    public NotificationUserId {
        if (value == null || value <= 0)
            throw new IllegalArgumentException("userId must be > 0");
    }
}
