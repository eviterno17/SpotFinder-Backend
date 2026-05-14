package com.spotfinderbackend.accesscontrol.domain.model.commands;

/** Issued by Payment Processing BC when a payment is successfully completed. */
public record MarkSessionAsPaidCommand(Long sessionId) {
    public MarkSessionAsPaidCommand {
        if (sessionId == null || sessionId <= 0)
            throw new IllegalArgumentException("sessionId must be > 0");
    }
}
