package com.spotfinderbackend.notifications.domain.model.events;

import com.spotfinderbackend.notifications.domain.model.valueobjects.NotificationChannel;
import com.spotfinderbackend.notifications.domain.model.valueobjects.NotificationType;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.time.LocalDateTime;

@Getter
public class NotificationSentEvent extends ApplicationEvent {
    private final Long notificationId;
    private final Long userId;
    private final NotificationType type;
    private final NotificationChannel channel;
    private final LocalDateTime sentAt;

    public NotificationSentEvent(Object source, Long notificationId, Long userId,
                                 NotificationType type, NotificationChannel channel, LocalDateTime sentAt) {
        super(source);
        this.notificationId = notificationId;
        this.userId = userId;
        this.type = type;
        this.channel = channel;
        this.sentAt = sentAt;
    }
}
