package com.spotfinderbackend.accesscontrol.application.acl;

import com.spotfinderbackend.accesscontrol.domain.model.commands.MarkSessionAsPaidCommand;
import com.spotfinderbackend.accesscontrol.domain.model.commands.OpenAllBarriersCommand;
import com.spotfinderbackend.accesscontrol.domain.model.valueobjects.SessionStatus;
import com.spotfinderbackend.accesscontrol.domain.services.AccessCommandService;
import com.spotfinderbackend.accesscontrol.infrastructure.persistence.jpa.repositories.VehicleSessionRepository;
import com.spotfinderbackend.accesscontrol.interfaces.acl.AccessControlContextFacade;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AccessControlContextFacadeImpl implements AccessControlContextFacade {

    private final VehicleSessionRepository sessionRepository;
    private final AccessCommandService accessCommandService;

    public AccessControlContextFacadeImpl(VehicleSessionRepository sessionRepository,
                                          AccessCommandService accessCommandService) {
        this.sessionRepository = sessionRepository;
        this.accessCommandService = accessCommandService;
    }

    @Override
    public Optional<SessionDetails> findSessionDetails(Long sessionId) {
        return sessionRepository.findById(sessionId)
                .map(s -> new SessionDetails(
                        s.getId(),
                        s.getLicensePlate().plateText(),
                        s.getUserId() == null ? null : s.getUserId().value(),
                        s.getEntryTimestamp(),
                        s.getExitTimestamp(),
                        s.getPaymentStatus().name(),
                        s.getSessionStatus().name(),
                        s.getSlotId() == null ? null : s.getSlotId().value()
                ));
    }

    @Override
    public void markSessionAsPaid(Long sessionId) {
        accessCommandService.handle(new MarkSessionAsPaidCommand(sessionId));
    }

    @Override
    public List<Long> getActiveSessionUserIds() {
        return sessionRepository.findAll().stream()
                .filter(s -> s.getSessionStatus() == SessionStatus.ACTIVE)
                .filter(s -> s.getUserId() != null && s.getUserId().value() != null)
                .map(s -> s.getUserId().value())
                .distinct()
                .toList();
    }

    @Override
    public void openAllBarriers(String reason) {
        accessCommandService.handle(new OpenAllBarriersCommand(reason));
    }

    @Override
    public long countCompletedSessionsBetween(LocalDateTime start, LocalDateTime end) {
        return sessionRepository.findAll().stream()
                .filter(s -> s.getSessionStatus() == SessionStatus.COMPLETED)
                .filter(s -> s.getExitTimestamp() != null)
                .filter(s -> (start == null || !s.getExitTimestamp().isBefore(start))
                        && (end == null || !s.getExitTimestamp().isAfter(end)))
                .count();
    }
}
