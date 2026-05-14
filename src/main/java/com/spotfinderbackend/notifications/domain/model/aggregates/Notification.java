package com.spotfinderbackend.notifications.domain.model.aggregates;

import com.spotfinderbackend.notifications.domain.model.commands.SendNotificationCommand;
import com.spotfinderbackend.notifications.domain.model.events.NotificationSentEvent;
import com.spotfinderbackend.notifications.domain.model.valueobjects.NotificationChannel;
import com.spotfinderbackend.notifications.domain.model.valueobjects.NotificationStatus;
import com.spotfinderbackend.notifications.domain.model.valueobjects.NotificationType;
import com.spotfinderbackend.notifications.domain.model.valueobjects.NotificationUserId;
import com.spotfinderbackend.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import com.spotfinderbackend.shared.domain.model.exceptions.BusinessRuleException;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
@Getter
public class Notification extends AuditableAbstractAggregateRoot<Notification> {

    @Embedded
    private NotificationUserId userId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private NotificationType type;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false, length = 500)
    private String body;

    @Column(length = 2000)
    private String data; // JSON-as-text payload

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 15)
    private NotificationStatus status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private NotificationChannel channel;

    @Column
    private LocalDateTime sentAt;

    @Column
    private LocalDateTime readAt;

    @Column(length = 500)
    private String errorDetail;

    protected Notification() {}

    public Notification(SendNotificationCommand command, NotificationChannel channel) {
        this.userId = new NotificationUserId(command.userId());
        this.type = NotificationType.from(command.type());
        this.title = command.title() == null ? "" : command.title();
        this.body = command.body() == null ? "" : command.body();
        this.data = serializeData(command.data());
        this.status = NotificationStatus.PENDING;
        this.channel = channel == null ? NotificationChannel.PUSH : channel;
    }

    public void markAsSent() {
        this.status = NotificationStatus.SENT;
        this.sentAt = LocalDateTime.now();
        registerEvent(new NotificationSentEvent(this, getId(),
                userId == null ? null : userId.value(), type, channel, sentAt));
    }

    public void markAsDelivered() {
        this.status = NotificationStatus.DELIVERED;
    }

    public void markAsRead() {
        if (this.status == NotificationStatus.READ)
            throw new BusinessRuleException("Notification already read");
        this.status = NotificationStatus.READ;
        this.readAt = LocalDateTime.now();
    }

    public void markAsFailed(String errorDetail) {
        this.status = NotificationStatus.FAILED;
        this.errorDetail = errorDetail;
    }

    public boolean isRead() { return this.status == NotificationStatus.READ; }
    public boolean isPending() { return this.status == NotificationStatus.PENDING; }

    private static String serializeData(java.util.Map<String, String> data) {
        if (data == null || data.isEmpty()) return "{}";
        StringBuilder sb = new StringBuilder("{");
        boolean first = true;
        for (var e : data.entrySet()) {
            if (!first) sb.append(",");
            sb.append("\"").append(escape(e.getKey())).append("\":\"")
                    .append(escape(e.getValue() == null ? "" : e.getValue())).append("\"");
            first = false;
        }
        return sb.append("}").toString();
    }

    private static String escape(String s) {
        return s.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}
