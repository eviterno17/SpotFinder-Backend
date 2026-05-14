package com.spotfinderbackend.notifications.domain.model.entities;

import com.spotfinderbackend.notifications.domain.model.valueobjects.NotificationType;
import com.spotfinderbackend.notifications.domain.model.valueobjects.NotificationUserId;
import com.spotfinderbackend.shared.domain.model.entities.AuditableModel;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "notification_preferences",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "notification_type"}))
@Getter
public class NotificationPreference extends AuditableModel {

    @Embedded
    private NotificationUserId userId;

    @Enumerated(EnumType.STRING)
    @Column(name = "notification_type", nullable = false, length = 30)
    private NotificationType notificationType;

    @Column(nullable = false)
    private boolean enabled;

    protected NotificationPreference() {}

    public NotificationPreference(Long userId, NotificationType type, boolean enabled) {
        this.userId = new NotificationUserId(userId);
        this.notificationType = type;
        this.enabled = enabled;
    }

    public void enable() { this.enabled = true; }
    public void disable() {
        if (this.notificationType == NotificationType.EMERGENCY_ALERT) return; // emergency cannot be disabled
        this.enabled = false;
    }
    public boolean isEnabled() { return enabled; }
}
