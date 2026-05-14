package com.spotfinderbackend.emergency.domain.services;

import com.spotfinderbackend.emergency.domain.model.aggregates.EmergencyAlert;
import com.spotfinderbackend.emergency.domain.model.queries.*;
import com.spotfinderbackend.emergency.domain.model.valueobjects.EmergencyStatusResponse;

import java.util.List;
import java.util.Optional;

public interface EmergencyQueryService {
    EmergencyStatusResponse handle(GetEmergencyStatusQuery query);
    Optional<EmergencyAlert> handle(GetEmergencyByIdQuery query);
    List<EmergencyAlert> handle(GetActiveEmergenciesQuery query);
    List<EmergencyAlert> handle(GetEmergencyHistoryQuery query);
}
