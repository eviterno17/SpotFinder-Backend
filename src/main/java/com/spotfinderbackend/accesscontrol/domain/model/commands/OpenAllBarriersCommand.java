package com.spotfinderbackend.accesscontrol.domain.model.commands;

public record OpenAllBarriersCommand(String reason) {
    public OpenAllBarriersCommand {
        if (reason == null || reason.isBlank()) reason = "MANUAL";
    }
}
