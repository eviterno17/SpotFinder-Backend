package com.spotfinderbackend.iam.domain.model.commands;

public record UpdateProfileCommand(Long userId, String firstName, String lastName) {
    public UpdateProfileCommand {
        if (userId == null || userId <= 0)
            throw new IllegalArgumentException("userId is required");
    }
}
