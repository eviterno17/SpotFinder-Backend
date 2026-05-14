package com.spotfinderbackend.accesscontrol.application.internal.queryservices;

import com.spotfinderbackend.accesscontrol.domain.model.aggregates.AccessBarrier;
import com.spotfinderbackend.accesscontrol.domain.model.aggregates.VehicleSession;
import com.spotfinderbackend.accesscontrol.domain.model.queries.*;
import com.spotfinderbackend.accesscontrol.domain.model.valueobjects.BarrierCode;
import com.spotfinderbackend.accesscontrol.domain.model.valueobjects.SessionStatus;
import com.spotfinderbackend.accesscontrol.domain.model.valueobjects.UserId;
import com.spotfinderbackend.accesscontrol.domain.services.VehicleSessionQueryService;
import com.spotfinderbackend.accesscontrol.infrastructure.persistence.jpa.repositories.AccessBarrierRepository;
import com.spotfinderbackend.accesscontrol.infrastructure.persistence.jpa.repositories.VehicleSessionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VehicleSessionQueryServiceImpl implements VehicleSessionQueryService {

    private final VehicleSessionRepository sessionRepository;
    private final AccessBarrierRepository barrierRepository;

    public VehicleSessionQueryServiceImpl(VehicleSessionRepository sessionRepository,
                                          AccessBarrierRepository barrierRepository) {
        this.sessionRepository = sessionRepository;
        this.barrierRepository = barrierRepository;
    }

    @Override
    public Optional<VehicleSession> handle(GetActiveSessionQuery query) {
        return sessionRepository.findByUserIdAndSessionStatus(new UserId(query.userId()), SessionStatus.ACTIVE);
    }

    @Override
    public Optional<VehicleSession> handle(GetSessionByIdQuery query) {
        return sessionRepository.findById(query.sessionId());
    }

    @Override
    public Optional<VehicleSession> handle(GetSessionByPlateQuery query) {
        if (query.licensePlate() == null) return Optional.empty();
        return sessionRepository.findByLicensePlate_PlateTextAndSessionStatus(
                query.licensePlate().toUpperCase().trim(), SessionStatus.ACTIVE);
    }

    @Override
    public List<VehicleSession> handle(GetSessionHistoryQuery query) {
        return sessionRepository.findByUserIdOrderByEntryTimestampDesc(new UserId(query.userId()));
    }

    @Override
    public Optional<AccessBarrier> handle(GetBarrierStatusQuery query) {
        if (query.barrierCode() == null) return Optional.empty();
        return barrierRepository.findByBarrierCode(new BarrierCode(query.barrierCode()));
    }
}
