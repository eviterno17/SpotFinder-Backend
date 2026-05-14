package com.spotfinderbackend.analytics.application.internal.outboundservices.acl;

import com.spotfinderbackend.analytics.domain.model.valueobjects.SlotStatusSnapshot;
import com.spotfinderbackend.parkingmonitoring.interfaces.acl.ParkingMonitoringContextFacade;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service("analyticsExternalParkingDataService")
public class ExternalParkingDataService {

    private final ParkingMonitoringContextFacade facade;

    public ExternalParkingDataService(ParkingMonitoringContextFacade facade) {
        this.facade = facade;
    }

    public List<SlotStatusSnapshot> getSlotStatusSnapshots(LocalDateTime start, LocalDateTime end, Long facilityId) {
        return facade.getSnapshotsBetween(start, end, facilityId).stream()
                .map(s -> new SlotStatusSnapshot(s.slotId(), s.status(), s.timestamp()))
                .toList();
    }

    public int getTotalSlots(Long facilityId) {
        return facade.countTotalSlots(facilityId);
    }
}
