package com.spotfinderbackend.accesscontrol.domain.model.commands;

import com.spotfinderbackend.accesscontrol.domain.model.valueobjects.BarrierPosition;

public record RegisterBarrierCommand(String barrierCode, BarrierPosition position, Long facilityId) {
    public RegisterBarrierCommand {
        if (barrierCode == null || barrierCode.isBlank())
            throw new IllegalArgumentException("barrierCode is required");
        if (position == null)
            throw new IllegalArgumentException("position is required");
    }
}
