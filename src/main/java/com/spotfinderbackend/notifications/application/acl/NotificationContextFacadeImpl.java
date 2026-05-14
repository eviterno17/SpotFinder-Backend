package com.spotfinderbackend.notifications.application.acl;

import com.spotfinderbackend.notifications.application.internal.services.DefaultPreferencesInitializer;
import com.spotfinderbackend.notifications.domain.model.commands.BroadcastNotificationCommand;
import com.spotfinderbackend.notifications.domain.model.commands.SendNotificationCommand;
import com.spotfinderbackend.notifications.domain.services.NotificationCommandService;
import com.spotfinderbackend.notifications.interfaces.acl.NotificationContextFacade;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class NotificationContextFacadeImpl implements NotificationContextFacade {

    private final NotificationCommandService commandService;
    private final DefaultPreferencesInitializer defaultPreferencesInitializer;

    public NotificationContextFacadeImpl(NotificationCommandService commandService,
                                         DefaultPreferencesInitializer defaultPreferencesInitializer) {
        this.commandService = commandService;
        this.defaultPreferencesInitializer = defaultPreferencesInitializer;
    }

    @Override
    public void sendNotification(Long userId, String type, String title, String body, Map<String, String> data) {
        commandService.handle(new SendNotificationCommand(userId, type, title, body, data == null ? Map.of() : data));
    }

    @Override
    public void broadcastNotification(List<Long> userIds, String type, String title, String body, Map<String, String> data) {
        commandService.handle(new BroadcastNotificationCommand(type, title, body,
                data == null ? Map.of() : data, userIds == null ? List.of() : userIds));
    }

    @Override
    public void initializeDefaultPreferencesFor(Long userId) {
        defaultPreferencesInitializer.initializeDefaultPreferences(userId);
    }
}
