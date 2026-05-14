package com.spotfinderbackend.parkingmonitoring.domain.services;

import com.spotfinderbackend.parkingmonitoring.domain.model.aggregates.ParkingFacility;
import com.spotfinderbackend.parkingmonitoring.domain.model.commands.RegisterFacilityCommand;

import java.util.Optional;

public interface ParkingFacilityCommandService {
    Optional<ParkingFacility> handle(RegisterFacilityCommand command);
}
