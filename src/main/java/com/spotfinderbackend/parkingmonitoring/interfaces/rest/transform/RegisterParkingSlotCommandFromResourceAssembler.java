package com.spotfinderbackend.parkingmonitoring.interfaces.rest.transform;

import com.spotfinderbackend.parkingmonitoring.domain.model.commands.RegisterParkingSlotCommand;
import com.spotfinderbackend.parkingmonitoring.interfaces.rest.resources.RegisterParkingSlotResource;

public class RegisterParkingSlotCommandFromResourceAssembler {
    public static RegisterParkingSlotCommand toCommandFromResource(RegisterParkingSlotResource r) {
        return new RegisterParkingSlotCommand(r.slotCode(), r.sensorId(), r.facilityId());
    }
}
