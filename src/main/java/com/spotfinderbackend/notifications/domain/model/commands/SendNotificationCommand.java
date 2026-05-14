package com.spotfinderbackend.notifications.domain.model.commands;

import java.util.Map;

public record SendNotificationCommand(Long userId, String type, String title, String body, Map<String, String> data) {
    public SendNotificationCommand {
        if (userId == null || userId <= 0)
            throw new IllegalArgumentException("userId must be > 0");
        if (type == null || type.isBlank())
            throw new IllegalArgumentException("type is required");
        if (data == null) data = Map.of();
    }
}
