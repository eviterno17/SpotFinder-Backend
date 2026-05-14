package com.spotfinderbackend.accesscontrol.application.internal.commandservices;

import com.spotfinderbackend.accesscontrol.domain.model.aggregates.VehicleSession;
import com.spotfinderbackend.accesscontrol.domain.model.commands.CreateVehicleSessionCommand;
import com.spotfinderbackend.accesscontrol.domain.model.commands.EndVehicleSessionCommand;
import com.spotfinderbackend.accesscontrol.domain.model.valueobjects.SessionStatus;
import com.spotfinderbackend.accesscontrol.domain.services.VehicleSessionCommandService;
import com.spotfinderbackend.accesscontrol.infrastructure.persistence.jpa.repositories.VehicleSessionRepository;
import com.spotfinderbackend.shared.domain.model.exceptions.ConflictException;
import com.spotfinderbackend.shared.domain.model.exceptions.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class VehicleSessionCommandServiceImpl implements VehicleSessionCommandService {

    private final VehicleSessionRepository repository;

    public VehicleSessionCommandServiceImpl(VehicleSessionRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<VehicleSession> handle(CreateVehicleSessionCommand command) {
        if (repository.existsByLicensePlate_PlateTextAndSessionStatus(
                command.licensePlate().toUpperCase().trim(), SessionStatus.ACTIVE))
            throw new ConflictException("Active session already exists for plate " + command.licensePlate());
        var session = new VehicleSession(command);
        return Optional.of(repository.save(session));
    }

    @Override
    public void handle(EndVehicleSessionCommand command) {
        var session = repository.findById(command.sessionId())
                .orElseThrow(() -> new NotFoundException("Session not found: " + command.sessionId()));
        session.end(command.exitTimestamp());
        repository.save(session);
    }
}
