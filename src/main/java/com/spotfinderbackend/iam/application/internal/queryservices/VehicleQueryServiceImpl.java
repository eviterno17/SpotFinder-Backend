package com.spotfinderbackend.iam.application.internal.queryservices;

import com.spotfinderbackend.iam.domain.model.aggregates.Vehicle;
import com.spotfinderbackend.iam.domain.model.queries.GetVehicleByPlateQuery;
import com.spotfinderbackend.iam.domain.model.queries.GetVehiclesByUserIdQuery;
import com.spotfinderbackend.iam.domain.model.valueobjects.Plate;
import com.spotfinderbackend.iam.domain.services.VehicleQueryService;
import com.spotfinderbackend.iam.infrastructure.persistence.jpa.repositories.VehicleRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VehicleQueryServiceImpl implements VehicleQueryService {

    private final VehicleRepository vehicleRepository;

    public VehicleQueryServiceImpl(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    @Override
    public List<Vehicle> handle(GetVehiclesByUserIdQuery query) {
        return vehicleRepository.findByUserId(query.userId());
    }

    @Override
    public Optional<Vehicle> handle(GetVehicleByPlateQuery query) {
        if (query.plate() == null || query.plate().isBlank()) return Optional.empty();
        return vehicleRepository.findByPlate(new Plate(query.plate()));
    }
}
