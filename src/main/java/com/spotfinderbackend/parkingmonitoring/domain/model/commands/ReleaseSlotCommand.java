package com.spotfinderbackend.parkingmonitoring.domain.model.commands;

/** Issued by Access Control BC when a vehicle session ends. */
public record ReleaseSlotCommand(Long slotId) {
    public ReleaseSlotCommand {
        if (slotId == null || slotId <= 0)
            throw new IllegalArgumentException("slotId must be > 0");
    }
}
