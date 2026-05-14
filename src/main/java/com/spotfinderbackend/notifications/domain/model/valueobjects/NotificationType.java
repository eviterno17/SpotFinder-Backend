package com.spotfinderbackend.notifications.domain.model.valueobjects;

public enum NotificationType {
    ENTRY_CONFIRMED,
    PAYMENT_REMINDER,
    PAYMENT_SUCCESS,
    EMERGENCY_ALERT,
    SESSION_END,
    PAYMENT_FAILED;

    public static NotificationType from(String value) {
        if (value == null) return null;
        return NotificationType.valueOf(value.toUpperCase());
    }

    public boolean isEmergency() {
        return this == EMERGENCY_ALERT;
    }
}
