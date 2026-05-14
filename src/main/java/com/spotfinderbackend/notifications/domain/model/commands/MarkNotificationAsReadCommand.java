package com.spotfinderbackend.notifications.domain.model.commands;

public record MarkNotificationAsReadCommand(Long notificationId) {
    public MarkNotificationAsReadCommand {
        if (notificationId == null || notificationId <= 0)
            throw new IllegalArgumentException("notificationId must be > 0");
    }
}
