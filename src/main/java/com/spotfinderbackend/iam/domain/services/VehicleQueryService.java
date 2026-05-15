package com.spotfinderbackend.iam.domain.services;

import com.spotfinderbackend.iam.domain.model.aggregates.Vehicle;
import com.spotfinderbackend.iam.domain.model.queries.GetVehicleByPlateQuery;
import com.spotfinderbackend.iam.domain.model.queries.GetVehiclesByUserIdQuery;

import java.util.List;
import java.util.Optional;

public interface VehicleQueryService {
    List<Vehicle> handle(GetVehiclesByUserIdQuery query);
    Optional<Vehicle> handle(GetVehicleByPlateQuery query);
}
