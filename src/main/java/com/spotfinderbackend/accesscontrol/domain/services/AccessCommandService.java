package com.spotfinderbackend.accesscontrol.domain.services;

import com.spotfinderbackend.accesscontrol.domain.model.aggregates.VehicleSession;
import com.spotfinderbackend.accesscontrol.domain.model.commands.*;
import com.spotfinderbackend.accesscontrol.domain.model.valueobjects.LicensePlate;

import java.util.Optional;

public interface AccessCommandService {
    Optional<VehicleSession> handle(RegisterEntryCommand command);
    void handle(RegisterExitCommand command);
    Optional<LicensePlate> handle(RecognizePlateCommand command);
    void handle(OpenAllBarriersCommand command);
    void handle(MarkSessionAsPaidCommand command);
}
