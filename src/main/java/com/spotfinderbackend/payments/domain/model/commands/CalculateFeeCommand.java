package com.spotfinderbackend.payments.domain.model.commands;

import java.time.LocalDateTime;

public record CalculateFeeCommand(Long sessionId, LocalDateTime entryTimestamp, LocalDateTime exitTimestamp) {
    public CalculateFeeCommand {
        if (sessionId == null || sessionId <= 0)
            throw new IllegalArgumentException("sessionId must be > 0");
    }
}
