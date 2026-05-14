package com.spotfinderbackend.accesscontrol.domain.services;

import com.spotfinderbackend.accesscontrol.domain.model.aggregates.AccessBarrier;
import com.spotfinderbackend.accesscontrol.domain.model.aggregates.VehicleSession;
import com.spotfinderbackend.accesscontrol.domain.model.queries.*;

import java.util.List;
import java.util.Optional;

public interface VehicleSessionQueryService {
    Optional<VehicleSession> handle(GetActiveSessionQuery query);
    Optional<VehicleSession> handle(GetSessionByIdQuery query);
    Optional<VehicleSession> handle(GetSessionByPlateQuery query);
    List<VehicleSession> handle(GetSessionHistoryQuery query);
    Optional<AccessBarrier> handle(GetBarrierStatusQuery query);
}
