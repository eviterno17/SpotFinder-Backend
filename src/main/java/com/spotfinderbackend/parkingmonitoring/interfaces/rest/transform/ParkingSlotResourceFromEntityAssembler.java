package com.spotfinderbackend.parkingmonitoring.interfaces.rest.transform;

import com.spotfinderbackend.parkingmonitoring.domain.model.aggregates.ParkingSlot;
import com.spotfinderbackend.parkingmonitoring.interfaces.rest.resources.ParkingSlotResource;

import java.util.Collection;
import java.util.List;

public class ParkingSlotResourceFromEntityAssembler {

    public static ParkingSlotResource toResourceFromEntity(ParkingSlot entity) {
        return new ParkingSlotResource(
                entity.getId(),
                entity.getSlotCode().code(),
                entity.getStatus().name(),
                entity.getSensorId() == null ? null : entity.getSensorId().value(),
                entity.getFacilityId() == null ? null : entity.getFacilityId().value(),
                entity.getLastUpdated()
        );
    }

    public static List<ParkingSlotResource> toResourcesFromEntities(Collection<ParkingSlot> entities) {
        return entities.stream().map(ParkingSlotResourceFromEntityAssembler::toResourceFromEntity).toList();
    }
}
