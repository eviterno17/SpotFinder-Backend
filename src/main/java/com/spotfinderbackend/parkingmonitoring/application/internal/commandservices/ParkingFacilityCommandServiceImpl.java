package com.spotfinderbackend.parkingmonitoring.application.internal.commandservices;

import com.spotfinderbackend.parkingmonitoring.domain.model.aggregates.ParkingFacility;
import com.spotfinderbackend.parkingmonitoring.domain.model.commands.RegisterFacilityCommand;
import com.spotfinderbackend.parkingmonitoring.domain.services.ParkingFacilityCommandService;
import com.spotfinderbackend.parkingmonitoring.infrastructure.persistence.jpa.repositories.ParkingFacilityRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class ParkingFacilityCommandServiceImpl implements ParkingFacilityCommandService {

    private final ParkingFacilityRepository parkingFacilityRepository;

    public ParkingFacilityCommandServiceImpl(ParkingFacilityRepository parkingFacilityRepository) {
        this.parkingFacilityRepository = parkingFacilityRepository;
    }

    @Override
    public Optional<ParkingFacility> handle(RegisterFacilityCommand command) {
        var facility = new ParkingFacility(command);
        return Optional.of(parkingFacilityRepository.save(facility));
    }
}
