package com.spotfinderbackend.iam.domain.services;

import com.spotfinderbackend.iam.domain.model.aggregates.User;
import com.spotfinderbackend.iam.domain.model.commands.SignInCommand;
import com.spotfinderbackend.iam.domain.model.commands.SignUpCommand;
import com.spotfinderbackend.iam.domain.model.commands.UpdateFcmTokenCommand;
import com.spotfinderbackend.iam.domain.model.commands.UpdateProfileCommand;

import java.util.Optional;

public interface UserCommandService {
    void handle(SignUpCommand command);

    void handle(SignInCommand command);

    Optional<User> handle(UpdateProfileCommand command);

    void handle(UpdateFcmTokenCommand command);
}
