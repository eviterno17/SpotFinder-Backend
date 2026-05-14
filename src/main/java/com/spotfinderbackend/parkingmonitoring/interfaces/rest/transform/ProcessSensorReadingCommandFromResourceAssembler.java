package com.spotfinderbackend.parkingmonitoring.interfaces.rest.transform;

import com.spotfinderbackend.parkingmonitoring.domain.model.commands.ProcessSensorReadingCommand;
import com.spotfinderbackend.parkingmonitoring.interfaces.rest.resources.SensorReadingResource;

import java.time.LocalDateTime;

public class ProcessSensorReadingCommandFromResourceAssembler {
    public static ProcessSensorReadingCommand toCommandFromResource(SensorReadingResource r) {
        return new ProcessSensorReadingCommand(
                r.sensorId(),
                r.slotId(),
                r.distance(),
                r.timestamp() == null ? LocalDateTime.now() : r.timestamp()
        );
    }
}
