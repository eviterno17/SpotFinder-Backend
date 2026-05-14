package com.spotfinderbackend.notifications.domain.model.commands;

import java.util.List;
import java.util.Map;

public record BroadcastNotificationCommand(String type, String title, String body,
                                            Map<String, String> data, List<Long> recipientUserIds) {
    public BroadcastNotificationCommand {
        if (type == null || type.isBlank())
            throw new IllegalArgumentException("type is required");
        if (recipientUserIds == null) recipientUserIds = List.of();
        if (data == null) data = Map.of();
    }
}
