package com.spotfinderbackend.iam.domain.services;

import com.spotfinderbackend.iam.domain.model.aggregates.Vehicle;
import com.spotfinderbackend.iam.domain.model.commands.DeleteVehicleCommand;
import com.spotfinderbackend.iam.domain.model.commands.RegisterVehicleCommand;

import java.util.Optional;

public interface VehicleCommandService {
    Optional<Vehicle> handle(RegisterVehicleCommand command);
    void handle(DeleteVehicleCommand command);
}
