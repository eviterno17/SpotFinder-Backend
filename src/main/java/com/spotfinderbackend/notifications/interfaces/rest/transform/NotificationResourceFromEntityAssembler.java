package com.spotfinderbackend.notifications.interfaces.rest.transform;

import com.spotfinderbackend.notifications.domain.model.aggregates.Notification;
import com.spotfinderbackend.notifications.domain.model.commands.SendNotificationCommand;
import com.spotfinderbackend.notifications.domain.model.commands.UpdatePreferencesCommand;
import com.spotfinderbackend.notifications.domain.model.entities.NotificationPreference;
import com.spotfinderbackend.notifications.interfaces.rest.resources.NotificationPreferenceResource;
import com.spotfinderbackend.notifications.interfaces.rest.resources.NotificationResource;
import com.spotfinderbackend.notifications.interfaces.rest.resources.SendNotificationResource;
import com.spotfinderbackend.notifications.interfaces.rest.resources.UpdatePreferencesResource;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NotificationResourceFromEntityAssembler {

    public static NotificationResource toResourceFromEntity(Notification n) {
        return new NotificationResource(
                n.getId(),
                n.getUserId() == null ? null : n.getUserId().value(),
                n.getType().name(),
                n.getTitle(),
                n.getBody(),
                n.getStatus().name(),
                n.getChannel().name(),
                n.getCreatedAt(),
                n.getReadAt()
        );
    }

    public static List<NotificationResource> toResourcesFromEntities(Collection<Notification> entities) {
        return entities.stream().map(NotificationResourceFromEntityAssembler::toResourceFromEntity).toList();
    }

    public static SendNotificationCommand toCommand(SendNotificationResource r) {
        return new SendNotificationCommand(r.userId(), r.type(), r.title(), r.body(),
                r.data() == null ? Map.of() : r.data());
    }

    public static NotificationPreferenceResource toResourceFromPreference(NotificationPreference p) {
        return new NotificationPreferenceResource(p.getNotificationType().name(), p.isEnabled());
    }

    public static List<NotificationPreferenceResource> toResourcesFromPreferences(Collection<NotificationPreference> ps) {
        return ps.stream().map(NotificationResourceFromEntityAssembler::toResourceFromPreference).toList();
    }

    public static UpdatePreferencesCommand toCommand(Long userId, UpdatePreferencesResource r) {
        Map<String, Boolean> map = new HashMap<>();
        if (r.preferences() != null) {
            r.preferences().forEach(p -> map.put(p.notificationType(), p.enabled()));
        }
        return new UpdatePreferencesCommand(userId, map);
    }
}
