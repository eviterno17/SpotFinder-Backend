package com.spotfinderbackend.emergency.interfaces.rest.transform;

import com.spotfinderbackend.emergency.domain.model.aggregates.EmergencyAlert;
import com.spotfinderbackend.emergency.domain.model.commands.TriggerEmergencyAlertCommand;
import com.spotfinderbackend.emergency.domain.model.valueobjects.EmergencyStatusResponse;
import com.spotfinderbackend.emergency.domain.model.valueobjects.EmergencyType;
import com.spotfinderbackend.emergency.interfaces.rest.resources.EmergencyAlertResource;
import com.spotfinderbackend.emergency.interfaces.rest.resources.EmergencyStatusResource;
import com.spotfinderbackend.emergency.interfaces.rest.resources.TriggerAlertResource;

import java.util.Collection;
import java.util.List;

public class EmergencyAlertResourceFromEntityAssembler {

    public static EmergencyAlertResource toResourceFromEntity(EmergencyAlert a) {
        return new EmergencyAlertResource(
                a.getId(),
                a.getSensorId() == null ? null : a.getSensorId().value(),
                a.getGasLevel(),
                a.getType() == null ? null : a.getType().name(),
                a.getStatus().name(),
                a.getTriggeredAt(),
                a.getResolvedAt(),
                a.getResolvedBy(),
                a.getSensorLocation()
        );
    }

    public static List<EmergencyAlertResource> toResourcesFromEntities(Collection<EmergencyAlert> entities) {
        return entities.stream().map(EmergencyAlertResourceFromEntityAssembler::toResourceFromEntity).toList();
    }

    public static TriggerEmergencyAlertCommand toCommand(TriggerAlertResource r) {
        return new TriggerEmergencyAlertCommand(r.sensorId(), r.gasLevel(),
                EmergencyType.valueOf(r.type().toUpperCase()), r.sensorLocation());
    }

    public static EmergencyStatusResource toResourceFromResponse(EmergencyStatusResponse r) {
        return new EmergencyStatusResource(
                r.isEmergencyActive(), r.emergencyId(),
                r.type() == null ? null : r.type().name(),
                r.gasLevel(), r.sensorLocation(), r.triggeredAt(), r.overallStatus()
        );
    }
}
