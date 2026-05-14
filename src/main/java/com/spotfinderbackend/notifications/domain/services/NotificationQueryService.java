package com.spotfinderbackend.notifications.domain.services;

import com.spotfinderbackend.notifications.domain.model.aggregates.Notification;
import com.spotfinderbackend.notifications.domain.model.entities.NotificationPreference;
import com.spotfinderbackend.notifications.domain.model.queries.*;

import java.util.List;
import java.util.Optional;

public interface NotificationQueryService {
    List<Notification> handle(GetNotificationsByUserQuery query);
    List<Notification> handle(GetUnreadNotificationsQuery query);
    Optional<Notification> handle(GetNotificationByIdQuery query);
    List<NotificationPreference> handle(GetPreferencesByUserQuery query);
}
