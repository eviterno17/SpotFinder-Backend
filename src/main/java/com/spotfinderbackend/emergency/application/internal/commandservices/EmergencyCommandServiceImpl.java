package com.spotfinderbackend.emergency.application.internal.commandservices;

import com.spotfinderbackend.emergency.domain.model.aggregates.EmergencyAlert;
import com.spotfinderbackend.emergency.domain.model.commands.ActivateEvacuationCommand;
import com.spotfinderbackend.emergency.domain.model.commands.ResolveEmergencyCommand;
import com.spotfinderbackend.emergency.domain.model.commands.TriggerEmergencyAlertCommand;
import com.spotfinderbackend.emergency.domain.model.valueobjects.EmergencyStatus;
import com.spotfinderbackend.emergency.domain.services.EmergencyCommandService;
import com.spotfinderbackend.emergency.domain.services.EmergencyThresholdService;
import com.spotfinderbackend.emergency.infrastructure.persistence.jpa.repositories.EmergencyAlertRepository;
import com.spotfinderbackend.shared.domain.model.exceptions.BusinessRuleException;
import com.spotfinderbackend.shared.domain.model.exceptions.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class EmergencyCommandServiceImpl implements EmergencyCommandService {

    private final EmergencyAlertRepository repository;
    private final EmergencyThresholdService thresholdService;

    public EmergencyCommandServiceImpl(EmergencyAlertRepository repository,
                                       EmergencyThresholdService thresholdService) {
        this.repository = repository;
        this.thresholdService = thresholdService;
    }

    @Override
    public Optional<EmergencyAlert> handle(TriggerEmergencyAlertCommand command) {
        if (!thresholdService.isDangerousCondition(command.gasLevel(), command.type())) {
            return Optional.empty();
        }
        var alert = new EmergencyAlert(command);
        return Optional.of(repository.save(alert));
    }

    @Override
    public void handle(ResolveEmergencyCommand command) {
        var alert = repository.findById(command.emergencyId())
                .orElseThrow(() -> new NotFoundException("Emergency not found: " + command.emergencyId()));
        if (alert.isResolved())
            throw new BusinessRuleException("Emergency already resolved");
        alert.resolve(command.adminUserId());
        repository.save(alert);
    }

    @Override
    public void handle(ActivateEvacuationCommand command) {
        if (!repository.existsByStatus(EmergencyStatus.ACTIVE))
            throw new BusinessRuleException("No active emergency to evacuate");
        // Re-publishing the trigger event is overkill; the active emergency's handler
        // already executed the protocol when triggered.
    }
}
