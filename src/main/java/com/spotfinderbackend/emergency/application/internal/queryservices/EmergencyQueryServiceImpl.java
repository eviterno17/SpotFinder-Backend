package com.spotfinderbackend.emergency.application.internal.queryservices;

import com.spotfinderbackend.emergency.domain.model.aggregates.EmergencyAlert;
import com.spotfinderbackend.emergency.domain.model.queries.*;
import com.spotfinderbackend.emergency.domain.model.valueobjects.EmergencyStatus;
import com.spotfinderbackend.emergency.domain.model.valueobjects.EmergencyStatusResponse;
import com.spotfinderbackend.emergency.domain.services.EmergencyQueryService;
import com.spotfinderbackend.emergency.infrastructure.persistence.jpa.repositories.EmergencyAlertRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class EmergencyQueryServiceImpl implements EmergencyQueryService {

    private final EmergencyAlertRepository repository;

    public EmergencyQueryServiceImpl(EmergencyAlertRepository repository) {
        this.repository = repository;
    }

    @Override
    public EmergencyStatusResponse handle(GetEmergencyStatusQuery query) {
        return repository.findFirstByStatusOrderByTriggeredAtDesc(EmergencyStatus.ACTIVE)
                .map(a -> new EmergencyStatusResponse(true, a.getId(), a.getType(), a.getGasLevel(),
                        a.getSensorLocation(), a.getTriggeredAt(), "EMERGENCY"))
                .orElse(EmergencyStatusResponse.normal());
    }

    @Override
    public Optional<EmergencyAlert> handle(GetEmergencyByIdQuery query) {
        return repository.findById(query.emergencyId());
    }

    @Override
    public List<EmergencyAlert> handle(GetActiveEmergenciesQuery query) {
        return repository.findByStatus(EmergencyStatus.ACTIVE);
    }

    @Override
    public List<EmergencyAlert> handle(GetEmergencyHistoryQuery query) {
        LocalDate start = query.startDate() == null ? LocalDate.now().minusMonths(1) : query.startDate();
        LocalDate end = query.endDate() == null ? LocalDate.now() : query.endDate();
        return repository.findByTriggeredAtBetween(start.atStartOfDay(), end.atTime(LocalDateTime.MAX.toLocalTime()));
    }
}
