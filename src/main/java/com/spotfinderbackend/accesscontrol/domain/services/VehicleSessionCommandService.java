package com.spotfinderbackend.accesscontrol.domain.services;

import com.spotfinderbackend.accesscontrol.domain.model.aggregates.VehicleSession;
import com.spotfinderbackend.accesscontrol.domain.model.commands.CreateVehicleSessionCommand;
import com.spotfinderbackend.accesscontrol.domain.model.commands.EndVehicleSessionCommand;

import java.util.Optional;

public interface VehicleSessionCommandService {
    Optional<VehicleSession> handle(CreateVehicleSessionCommand command);
    void handle(EndVehicleSessionCommand command);
}
