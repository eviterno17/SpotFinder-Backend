package com.spotfinderbackend.accesscontrol.domain.model.commands;

public record RegisterEntryCommand(byte[] imageData, String barrierCode) {
    public RegisterEntryCommand {
        if (imageData == null || imageData.length == 0)
            throw new IllegalArgumentException("imageData is required");
        if (barrierCode == null || barrierCode.isBlank())
            throw new IllegalArgumentException("barrierCode is required");
    }
}
