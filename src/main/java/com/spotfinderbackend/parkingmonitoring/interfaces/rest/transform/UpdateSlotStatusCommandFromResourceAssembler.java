package com.spotfinderbackend.parkingmonitoring.interfaces.rest.transform;

import com.spotfinderbackend.parkingmonitoring.domain.model.commands.UpdateSlotStatusCommand;
import com.spotfinderbackend.parkingmonitoring.domain.model.valueobjects.SlotStatus;
import com.spotfinderbackend.parkingmonitoring.interfaces.rest.resources.SlotStatusUpdateResource;

public class UpdateSlotStatusCommandFromResourceAssembler {
    public static UpdateSlotStatusCommand toCommandFromResource(Long slotId, SlotStatusUpdateResource r) {
        return new UpdateSlotStatusCommand(slotId, SlotStatus.valueOf(r.status().toUpperCase()));
    }
}
