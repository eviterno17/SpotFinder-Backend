package com.spotfinderbackend.iam.domain.model.commands;

import com.spotfinderbackend.shared.domain.model.exceptions.BadRequestException;

/**
 * Command used to update an authenticated user's profile fields
 * (first name / last name). Email is intentionally immutable here —
 * changing it would require a separate verified flow.
 */
public record UpdateProfileCommand(Long userId, String firstName, String lastName) {
    public UpdateProfileCommand {
        if (userId == null || userId <= 0)
            throw new BadRequestException("userId is required");
    }
}
