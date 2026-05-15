package com.spotfinderbackend.iam.domain.model.commands;

public record UpdateFcmTokenCommand(Long userId, String fcmToken) {
    public UpdateFcmTokenCommand {
        if (userId == null || userId <= 0)
            throw new IllegalArgumentException("userId is required");
        if (fcmToken == null || fcmToken.isBlank())
            throw new IllegalArgumentException("fcmToken is required");
    }
}
