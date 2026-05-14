package com.spotfinderbackend.parkingmonitoring.domain.model.commands;

import com.spotfinderbackend.parkingmonitoring.domain.model.valueobjects.SlotStatus;

public record UpdateSlotStatusCommand(Long slotId, SlotStatus newStatus) {
    public UpdateSlotStatusCommand {
        if (slotId == null || slotId <= 0)
            throw new IllegalArgumentException("slotId must be > 0");
        if (newStatus == null)
            throw new IllegalArgumentException("newStatus is required");
    }
}
