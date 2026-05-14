package com.spotfinderbackend.notifications.domain.services;

import com.spotfinderbackend.notifications.domain.model.entities.NotificationPreference;
import com.spotfinderbackend.notifications.domain.model.valueobjects.NotificationType;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PreferenceValidationService {

    public boolean shouldSendNotification(Long userId, NotificationType type,
                                          List<NotificationPreference> preferences) {
        if (isEmergencyType(type)) return true;
        if (preferences == null || preferences.isEmpty()) return true; // default: opt-in
        return preferences.stream()
                .filter(p -> p.getNotificationType() == type)
                .findFirst()
                .map(NotificationPreference::isEnabled)
                .orElse(true);
    }

    public boolean isEmergencyType(NotificationType type) {
        return type != null && type.isEmergency();
    }
}
