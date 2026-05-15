package com.spotfinderbackend.iam.domain.model.commands;

import com.spotfinderbackend.shared.domain.model.exceptions.BadRequestException;

/**
 * Command used by an authenticated driver to change their own password.
 * The current password is required so the operation fails fast if the
 * session has been hijacked.
 */
public record ChangePasswordCommand(Long userId, String currentPassword, String newPassword) {
    public ChangePasswordCommand {
        if (userId == null || userId <= 0)
            throw new BadRequestException("userId is required");

        if (currentPassword == null || currentPassword.isBlank())
            throw new BadRequestException("Current password cannot be empty.");

        if (newPassword == null || newPassword.isBlank())
            throw new BadRequestException("New password cannot be empty.");

        if (newPassword.length() < 8)
            throw new BadRequestException("New password must be at least 8 characters long.");

        if (newPassword.equals(currentPassword))
            throw new BadRequestException("New password must be different from the current one.");
    }
}
