package com.spotfinderbackend.accesscontrol.application.internal.eventhandlers;

import com.spotfinderbackend.accesscontrol.application.internal.outboundservices.acl.ExternalParkingMonitoringService;
import com.spotfinderbackend.accesscontrol.domain.model.events.VehicleSessionEndedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
public class VehicleSessionEndedEventHandler {

    private final ExternalParkingMonitoringService externalParkingMonitoringService;

    public VehicleSessionEndedEventHandler(ExternalParkingMonitoringService externalParkingMonitoringService) {
        this.externalParkingMonitoringService = externalParkingMonitoringService;
    }

    @EventListener
    public void on(VehicleSessionEndedEvent event) {
        if (event.getSlotId() != null) {
            externalParkingMonitoringService.releaseSlot(event.getSlotId());
        }
    }
}
