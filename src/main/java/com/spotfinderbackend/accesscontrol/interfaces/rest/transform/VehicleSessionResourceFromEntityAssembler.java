package com.spotfinderbackend.accesscontrol.interfaces.rest.transform;

import com.spotfinderbackend.accesscontrol.domain.model.aggregates.VehicleSession;
import com.spotfinderbackend.accesscontrol.interfaces.rest.resources.VehicleSessionResource;

import java.util.Collection;
import java.util.List;

public class VehicleSessionResourceFromEntityAssembler {

    public static VehicleSessionResource toResourceFromEntity(VehicleSession s) {
        var dur = s.calculateDuration();
        long hours = dur.toHours();
        long minutes = dur.toMinutesPart();
        return new VehicleSessionResource(
                s.getId(),
                s.getLicensePlate().plateText(),
                s.getEntryTimestamp(),
                s.getExitTimestamp(),
                s.getSlotId() == null ? null : s.getSlotId().value(),
                s.getPaymentStatus().name(),
                s.getSessionStatus().name(),
                hours + "h " + minutes + "min",
                s.getUserId() == null ? null : s.getUserId().value()
        );
    }

    public static List<VehicleSessionResource> toResourcesFromEntities(Collection<VehicleSession> entities) {
        return entities.stream().map(VehicleSessionResourceFromEntityAssembler::toResourceFromEntity).toList();
    }
}
