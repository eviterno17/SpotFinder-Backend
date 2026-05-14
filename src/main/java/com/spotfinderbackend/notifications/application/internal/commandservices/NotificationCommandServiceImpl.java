package com.spotfinderbackend.notifications.application.internal.commandservices;

import com.spotfinderbackend.notifications.domain.model.aggregates.Notification;
import com.spotfinderbackend.notifications.domain.model.commands.*;
import com.spotfinderbackend.notifications.domain.model.entities.NotificationPreference;
import com.spotfinderbackend.notifications.domain.model.valueobjects.NotificationChannel;
import com.spotfinderbackend.notifications.domain.model.valueobjects.NotificationType;
import com.spotfinderbackend.notifications.domain.services.*;
import com.spotfinderbackend.notifications.infrastructure.persistence.jpa.repositories.NotificationPreferenceRepository;
import com.spotfinderbackend.notifications.infrastructure.persistence.jpa.repositories.NotificationRepository;
import com.spotfinderbackend.shared.domain.model.exceptions.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class NotificationCommandServiceImpl implements NotificationCommandService {

    private static final Logger LOG = LoggerFactory.getLogger(NotificationCommandServiceImpl.class);

    private final NotificationRepository notificationRepository;
    private final NotificationPreferenceRepository preferenceRepository;
    private final PushMessagingService pushMessagingService;
    private final FcmTokenService fcmTokenService;
    private final PreferenceValidationService preferenceValidationService;
    private final TemplateResolverService templateResolverService;

    public NotificationCommandServiceImpl(NotificationRepository notificationRepository,
                                          NotificationPreferenceRepository preferenceRepository,
                                          PushMessagingService pushMessagingService,
                                          FcmTokenService fcmTokenService,
                                          PreferenceValidationService preferenceValidationService,
                                          TemplateResolverService templateResolverService) {
        this.notificationRepository = notificationRepository;
        this.preferenceRepository = preferenceRepository;
        this.pushMessagingService = pushMessagingService;
        this.fcmTokenService = fcmTokenService;
        this.preferenceValidationService = preferenceValidationService;
        this.templateResolverService = templateResolverService;
    }

    @Override
    public Optional<Notification> handle(SendNotificationCommand command) {
        NotificationType type = NotificationType.from(command.type());
        var preferences = preferenceRepository.findByUserId_Value(command.userId());

        if (!preferenceValidationService.shouldSendNotification(command.userId(), type, preferences)) {
            return Optional.empty();
        }

        String title = command.title();
        String body = command.body();
        if (title == null || body == null || title.isBlank() || body.isBlank()) {
            var resolved = templateResolverService.resolveTemplate(type, command.data());
            title = title == null || title.isBlank() ? resolved.title() : title;
            body = body == null || body.isBlank() ? resolved.body() : body;
        }

        var enriched = new SendNotificationCommand(command.userId(), command.type(), title, body, command.data());
        var fcmTokenOpt = fcmTokenService.getFcmToken(command.userId());
        NotificationChannel channel = fcmTokenOpt.isPresent() ? NotificationChannel.PUSH : NotificationChannel.IN_APP;

        Notification notification = new Notification(enriched, channel);
        if (channel == NotificationChannel.PUSH) {
            try {
                boolean sent = pushMessagingService.sendPushNotification(fcmTokenOpt.get(), title, body, command.data());
                if (sent) notification.markAsSent();
                else notification.markAsFailed("FCM rejected");
            } catch (Exception e) {
                LOG.warn("FCM push failed: {}", e.getMessage());
                notification.markAsFailed(e.getMessage());
            }
        } else {
            notification.markAsSent(); // in-app delivered as soon as stored
        }
        return Optional.of(notificationRepository.save(notification));
    }

    @Override
    public List<Notification> handle(BroadcastNotificationCommand command) {
        List<Notification> persisted = new ArrayList<>();
        if (command.recipientUserIds() == null || command.recipientUserIds().isEmpty()) return persisted;

        var tokenMap = fcmTokenService.getFcmTokens(command.recipientUserIds());
        for (Long userId : command.recipientUserIds()) {
            var single = new SendNotificationCommand(userId, command.type(), command.title(), command.body(), command.data());
            NotificationChannel channel = tokenMap.containsKey(userId) ? NotificationChannel.PUSH : NotificationChannel.IN_APP;
            Notification notification = new Notification(single, channel);
            notification.markAsSent();
            persisted.add(notificationRepository.save(notification));
        }

        if (!tokenMap.isEmpty()) {
            pushMessagingService.sendBulkPushNotifications(
                    new ArrayList<>(tokenMap.values()), command.title(), command.body(), command.data());
        }
        return persisted;
    }

    @Override
    public void handle(MarkNotificationAsReadCommand command) {
        var n = notificationRepository.findById(command.notificationId())
                .orElseThrow(() -> new NotFoundException("Notification not found: " + command.notificationId()));
        n.markAsRead();
        notificationRepository.save(n);
    }

    @Override
    public void handle(UpdatePreferencesCommand command) {
        command.preferences().forEach((typeStr, enabled) -> {
            NotificationType type;
            try {
                type = NotificationType.valueOf(typeStr.toUpperCase());
            } catch (IllegalArgumentException ignored) {
                return;
            }
            if (type == NotificationType.EMERGENCY_ALERT) return; // emergency cannot be disabled
            var existing = preferenceRepository.findByUserId_ValueAndNotificationType(command.userId(), type);
            if (existing.isPresent()) {
                if (Boolean.TRUE.equals(enabled)) existing.get().enable();
                else existing.get().disable();
                preferenceRepository.save(existing.get());
            } else {
                preferenceRepository.save(new NotificationPreference(command.userId(), type, Boolean.TRUE.equals(enabled)));
            }
        });
    }
}
