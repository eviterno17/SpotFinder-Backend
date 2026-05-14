package com.spotfinderbackend.notifications.domain.services;

import com.spotfinderbackend.notifications.domain.model.aggregates.Notification;
import com.spotfinderbackend.notifications.domain.model.commands.*;

import java.util.List;
import java.util.Optional;

public interface NotificationCommandService {
    Optional<Notification> handle(SendNotificationCommand command);
    List<Notification> handle(BroadcastNotificationCommand command);
    void handle(MarkNotificationAsReadCommand command);
    void handle(UpdatePreferencesCommand command);
}
