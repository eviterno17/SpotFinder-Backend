package com.spotfinderbackend.accesscontrol.application.internal.commandservices;

import com.spotfinderbackend.accesscontrol.application.internal.outboundservices.acl.ExternalIamService;
import com.spotfinderbackend.accesscontrol.application.internal.outboundservices.acl.ExternalNotificationService;
import com.spotfinderbackend.accesscontrol.domain.model.aggregates.AccessBarrier;
import com.spotfinderbackend.accesscontrol.domain.model.aggregates.VehicleSession;
import com.spotfinderbackend.accesscontrol.domain.model.commands.*;
import com.spotfinderbackend.accesscontrol.domain.model.valueobjects.BarrierCode;
import com.spotfinderbackend.accesscontrol.domain.model.valueobjects.BarrierPosition;
import com.spotfinderbackend.accesscontrol.domain.model.valueobjects.LicensePlate;
import com.spotfinderbackend.accesscontrol.domain.model.valueobjects.SessionStatus;
import com.spotfinderbackend.accesscontrol.domain.services.AccessCommandService;
import com.spotfinderbackend.accesscontrol.domain.services.PlateRecognitionService;
import com.spotfinderbackend.accesscontrol.domain.services.VehicleSessionCommandService;
import com.spotfinderbackend.accesscontrol.infrastructure.persistence.jpa.repositories.AccessBarrierRepository;
import com.spotfinderbackend.accesscontrol.infrastructure.persistence.jpa.repositories.VehicleSessionRepository;
import com.spotfinderbackend.shared.domain.model.exceptions.BusinessRuleException;
import com.spotfinderbackend.shared.domain.model.exceptions.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Transactional
public class AccessCommandServiceImpl implements AccessCommandService {

    private static final Logger LOG = LoggerFactory.getLogger(AccessCommandServiceImpl.class);

    private final VehicleSessionRepository sessionRepository;
    private final AccessBarrierRepository barrierRepository;
    private final PlateRecognitionService plateRecognitionService;
    private final VehicleSessionCommandService vehicleSessionCommandService;
    private final ExternalIamService externalIamService;
    private final ExternalNotificationService externalNotificationService;

    public AccessCommandServiceImpl(VehicleSessionRepository sessionRepository,
                                    AccessBarrierRepository barrierRepository,
                                    PlateRecognitionService plateRecognitionService,
                                    VehicleSessionCommandService vehicleSessionCommandService,
                                    ExternalIamService externalIamService,
                                    ExternalNotificationService externalNotificationService) {
        this.sessionRepository = sessionRepository;
        this.barrierRepository = barrierRepository;
        this.plateRecognitionService = plateRecognitionService;
        this.vehicleSessionCommandService = vehicleSessionCommandService;
        this.externalIamService = externalIamService;
        this.externalNotificationService = externalNotificationService;
    }

    @Override
    public Optional<VehicleSession> handle(RegisterEntryCommand command) {
        var plateOpt = plateRecognitionService.recognizePlate(command.imageData());
        if (plateOpt.isEmpty()) {
            LOG.warn("ALPR could not recognize plate from entry image; barrier {}", command.barrierCode());
            return Optional.empty();
        }
        var plate = plateOpt.get();
        var userId = externalIamService.findUserIdByLicensePlate(plate.plateText()).orElse(null);

        var sessionOpt = vehicleSessionCommandService.handle(
                new CreateVehicleSessionCommand(plate.plateText(), LocalDateTime.now(), userId)
        );

        openBarrier(command.barrierCode(), "PLATE_RECOGNIZED");

        sessionOpt.ifPresent(s -> externalNotificationService.sendEntryNotification(
                userId, plate.plateText(), s.getEntryTimestamp()));
        return sessionOpt;
    }

    @Override
    public void handle(RegisterExitCommand command) {
        var plateOpt = plateRecognitionService.recognizePlate(command.imageData());
        if (plateOpt.isEmpty())
            throw new NotFoundException("Plate could not be recognized");

        var plate = plateOpt.get();
        var session = sessionRepository
                .findByLicensePlate_PlateTextAndSessionStatus(plate.plateText(), SessionStatus.ACTIVE)
                .orElseThrow(() -> new NotFoundException("Active session not found for plate " + plate.plateText()));

        if (!session.isPaid()) {
            externalNotificationService.sendPaymentReminderNotification(
                    session.getUserId() == null ? null : session.getUserId().value(), session.getId());
            throw new BusinessRuleException("Payment required before exit");
        }

        vehicleSessionCommandService.handle(new EndVehicleSessionCommand(session.getId(), LocalDateTime.now()));
        openBarrier(command.barrierCode(), "PAYMENT_VERIFIED");

        externalNotificationService.sendSessionEndNotification(
                session.getUserId() == null ? null : session.getUserId().value(),
                plate.plateText());
    }

    @Override
    public Optional<LicensePlate> handle(RecognizePlateCommand command) {
        return plateRecognitionService.recognizePlate(command.imageData());
    }

    @Override
    public void handle(OpenAllBarriersCommand command) {
        barrierRepository.findAll().forEach(b -> {
            b.forceOpen(command.reason());
            barrierRepository.save(b);
        });
    }

    @Override
    public void handle(MarkSessionAsPaidCommand command) {
        var session = sessionRepository.findById(command.sessionId())
                .orElseThrow(() -> new NotFoundException("Session not found: " + command.sessionId()));
        session.markAsPaid();
        sessionRepository.save(session);
    }

    private void openBarrier(String barrierCode, String reason) {
        AccessBarrier barrier = barrierRepository.findByBarrierCode(new BarrierCode(barrierCode))
                .orElseGet(() -> {
                    // Auto-create barrier on first use to keep dev/test flows simple.
                    var fresh = new AccessBarrier(new RegisterBarrierCommand(barrierCode,
                            barrierCode.toUpperCase().contains("EXIT") ? BarrierPosition.EXIT : BarrierPosition.ENTRY,
                            null));
                    return barrierRepository.save(fresh);
                });
        barrier.open(reason);
        barrierRepository.save(barrier);
    }
}
