package com.spotfinderbackend.parkingmonitoring.domain.services;

import com.spotfinderbackend.parkingmonitoring.domain.model.aggregates.ParkingSlot;
import com.spotfinderbackend.parkingmonitoring.domain.model.commands.*;

import java.util.Optional;

public interface ParkingSlotCommandService {
    Optional<ParkingSlot> handle(RegisterParkingSlotCommand command);
    void handle(UpdateSlotStatusCommand command);
    void handle(ProcessSensorReadingCommand command);
    void handle(SetAllLedsToEvacuationModeCommand command);
    void handle(RestoreLedsToNormalModeCommand command);
    void handle(ReleaseSlotCommand command);
}
