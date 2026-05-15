package com.spotfinderbackend.iam.application.internal.commandservices;

import com.spotfinderbackend.iam.domain.model.aggregates.Vehicle;
import com.spotfinderbackend.iam.domain.model.commands.DeleteVehicleCommand;
import com.spotfinderbackend.iam.domain.model.commands.RegisterVehicleCommand;
import com.spotfinderbackend.iam.domain.model.exceptions.VehicleAlreadyRegisteredException;
import com.spotfinderbackend.iam.domain.model.exceptions.VehicleNotFoundException;
import com.spotfinderbackend.iam.domain.model.valueobjects.Plate;
import com.spotfinderbackend.iam.domain.services.VehicleCommandService;
import com.spotfinderbackend.iam.infrastructure.persistence.jpa.repositories.UserRepository;
import com.spotfinderbackend.iam.infrastructure.persistence.jpa.repositories.VehicleRepository;
import com.spotfinderbackend.shared.domain.model.exceptions.ForbiddenException;
import com.spotfinderbackend.shared.domain.model.exceptions.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class VehicleCommandServiceImpl implements VehicleCommandService {

    private final VehicleRepository vehicleRepository;
    private final UserRepository userRepository;

    public VehicleCommandServiceImpl(VehicleRepository vehicleRepository, UserRepository userRepository) {
        this.vehicleRepository = vehicleRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Optional<Vehicle> handle(RegisterVehicleCommand command) {
        if (!userRepository.existsById(command.userId()))
            throw new NotFoundException("User not found: " + command.userId());

        Plate plate = new Plate(command.plate());
        if (vehicleRepository.existsByPlate(plate))
            throw new VehicleAlreadyRegisteredException(plate.value());

        Vehicle vehicle = new Vehicle(command.userId(), command.plate(),
                command.brand(), command.model(), command.color());
        return Optional.of(vehicleRepository.save(vehicle));
    }

    @Override
    public void handle(DeleteVehicleCommand command) {
        Vehicle vehicle = vehicleRepository.findById(command.vehicleId())
                .orElseThrow(() -> new VehicleNotFoundException(command.vehicleId()));
        if (!vehicle.getUserId().equals(command.userId()))
            throw new ForbiddenException("Vehicle does not belong to user " + command.userId());
        vehicleRepository.delete(vehicle);
    }
}
