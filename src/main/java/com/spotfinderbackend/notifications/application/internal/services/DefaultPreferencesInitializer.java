package com.spotfinderbackend.notifications.application.internal.services;

import com.spotfinderbackend.notifications.domain.model.entities.NotificationPreference;
import com.spotfinderbackend.notifications.domain.model.valueobjects.NotificationType;
import com.spotfinderbackend.notifications.infrastructure.persistence.jpa.repositories.NotificationPreferenceRepository;
import org.springframework.stereotype.Service;

@Service
public class DefaultPreferencesInitializer {

    private final NotificationPreferenceRepository preferenceRepository;

    public DefaultPreferencesInitializer(NotificationPreferenceRepository preferenceRepository) {
        this.preferenceRepository = preferenceRepository;
    }

    public void initializeDefaultPreferences(Long userId) {
        if (userId == null) return;
        if (preferenceRepository.existsByUserId_Value(userId)) return;
        for (NotificationType type : NotificationType.values()) {
            preferenceRepository.save(new NotificationPreference(userId, type, true));
        }
    }
}
