package com.spotfinderbackend.accesscontrol.domain.model.commands;

public record RecognizePlateCommand(byte[] imageData, String cameraPosition) {
    public RecognizePlateCommand {
        if (imageData == null || imageData.length == 0)
            throw new IllegalArgumentException("imageData is required");
        if (cameraPosition == null || cameraPosition.isBlank())
            throw new IllegalArgumentException("cameraPosition is required");
    }
}
