package com.spotfinderbackend.notifications.application.internal.queryservices;

import com.spotfinderbackend.notifications.domain.model.aggregates.Notification;
import com.spotfinderbackend.notifications.domain.model.entities.NotificationPreference;
import com.spotfinderbackend.notifications.domain.model.queries.*;
import com.spotfinderbackend.notifications.domain.model.valueobjects.NotificationStatus;
import com.spotfinderbackend.notifications.domain.model.valueobjects.NotificationType;
import com.spotfinderbackend.notifications.domain.services.NotificationQueryService;
import com.spotfinderbackend.notifications.infrastructure.persistence.jpa.repositories.NotificationPreferenceRepository;
import com.spotfinderbackend.notifications.infrastructure.persistence.jpa.repositories.NotificationRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class NotificationQueryServiceImpl implements NotificationQueryService {

    private final NotificationRepository notificationRepository;
    private final NotificationPreferenceRepository preferenceRepository;

    public NotificationQueryServiceImpl(NotificationRepository notificationRepository,
                                        NotificationPreferenceRepository preferenceRepository) {
        this.notificationRepository = notificationRepository;
        this.preferenceRepository = preferenceRepository;
    }

    @Override
    public List<Notification> handle(GetNotificationsByUserQuery query) {
        return notificationRepository.findByUserId_ValueOrderByCreatedAtDesc(query.userId());
    }

    @Override
    public List<Notification> handle(GetUnreadNotificationsQuery query) {
        return notificationRepository.findByUserId_ValueAndStatusNot(query.userId(), NotificationStatus.READ);
    }

    @Override
    public Optional<Notification> handle(GetNotificationByIdQuery query) {
        return notificationRepository.findById(query.notificationId());
    }

    @Override
    public List<NotificationPreference> handle(GetPreferencesByUserQuery query) {
        var existing = preferenceRepository.findByUserId_Value(query.userId());
        if (!existing.isEmpty()) return existing;
        // Return defaults when user has no preferences configured.
        List<NotificationPreference> defaults = new ArrayList<>();
        for (NotificationType type : NotificationType.values()) {
            defaults.add(new NotificationPreference(query.userId(), type, true));
        }
        return defaults;
    }
}
