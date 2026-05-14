package com.spotfinderbackend.emergency.domain.services;

import com.spotfinderbackend.emergency.domain.model.aggregates.EmergencyAlert;
import com.spotfinderbackend.emergency.domain.model.commands.ActivateEvacuationCommand;
import com.spotfinderbackend.emergency.domain.model.commands.ResolveEmergencyCommand;
import com.spotfinderbackend.emergency.domain.model.commands.TriggerEmergencyAlertCommand;

import java.util.Optional;

public interface EmergencyCommandService {
    Optional<EmergencyAlert> handle(TriggerEmergencyAlertCommand command);
    void handle(ResolveEmergencyCommand command);
    void handle(ActivateEvacuationCommand command);
}
