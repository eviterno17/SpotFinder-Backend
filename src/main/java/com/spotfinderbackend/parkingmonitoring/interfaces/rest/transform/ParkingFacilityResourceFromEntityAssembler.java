package com.spotfinderbackend.parkingmonitoring.interfaces.rest.transform;

import com.spotfinderbackend.parkingmonitoring.domain.model.aggregates.ParkingFacility;
import com.spotfinderbackend.parkingmonitoring.interfaces.rest.resources.ParkingFacilityResource;
import com.spotfinderbackend.parkingmonitoring.interfaces.rest.resources.RegisterFacilityResource;
import com.spotfinderbackend.parkingmonitoring.domain.model.commands.RegisterFacilityCommand;

public class ParkingFacilityResourceFromEntityAssembler {

    public static ParkingFacilityResource toResourceFromEntity(ParkingFacility f) {
        return new ParkingFacilityResource(f.getId(), f.getName().value(), f.getTotalSlots(),
                f.getAddress() == null ? "" : f.getAddress().value());
    }

    public static RegisterFacilityCommand toCommandFromResource(RegisterFacilityResource r) {
        return new RegisterFacilityCommand(r.name(), r.totalSlots(), r.address());
    }
}
