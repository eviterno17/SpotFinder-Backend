package com.spotfinderbackend.notifications.domain.model.commands;

import java.util.Map;

public record UpdatePreferencesCommand(Long userId, Map<String, Boolean> preferences) {
    public UpdatePreferencesCommand {
        if (userId == null || userId <= 0)
            throw new IllegalArgumentException("userId must be > 0");
        if (preferences == null) preferences = Map.of();
    }
}
