package com.spotfinderbackend.accesscontrol.application.internal.eventhandlers;

import com.spotfinderbackend.accesscontrol.domain.model.valueobjects.SessionStatus;
import com.spotfinderbackend.accesscontrol.infrastructure.persistence.jpa.repositories.VehicleSessionRepository;
import com.spotfinderbackend.parkingmonitoring.domain.model.events.SlotStatusChangedEvent;
import com.spotfinderbackend.parkingmonitoring.domain.model.valueobjects.SlotStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

/**
 * Correlates a freshly OCCUPIED slot with the most recent vehicle session that
 * still has no slot assigned. This enables the "Find My Car" feature: the
 * driver entered through the barrier, then parked, the ultrasonic sensor saw
 * the car, and we record which slot the session ended up in.
 */
@Service
public class SlotOccupiedSessionLinker {

    private static final Logger LOG = LoggerFactory.getLogger(SlotOccupiedSessionLinker.class);

    private final VehicleSessionRepository vehicleSessionRepository;

    public SlotOccupiedSessionLinker(VehicleSessionRepository vehicleSessionRepository) {
        this.vehicleSessionRepository = vehicleSessionRepository;
    }

    @EventListener
    public void on(SlotStatusChangedEvent event) {
        if (event.getNewStatus() != SlotStatus.OCCUPIED) return;

        var sessionOpt = vehicleSessionRepository
                .findFirstBySessionStatusAndSlotId_ValueIsNullOrderByEntryTimestampDesc(SessionStatus.ACTIVE);
        if (sessionOpt.isEmpty()) {
            LOG.debug("Slot {} became OCCUPIED but no active session without slot to link", event.getSlotId());
            return;
        }
        var session = sessionOpt.get();
        session.assignSlot(event.getSlotId());
        vehicleSessionRepository.save(session);
        LOG.info("Linked slot {} ({}) to vehicle session {} (plate {})",
                event.getSlotId(), event.getSlotCode(),
                session.getId(), session.getLicensePlate().plateText());
    }
}
