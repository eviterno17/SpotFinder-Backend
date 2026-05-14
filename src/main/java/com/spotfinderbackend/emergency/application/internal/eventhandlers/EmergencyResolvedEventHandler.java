package com.spotfinderbackend.emergency.application.internal.eventhandlers;

import com.spotfinderbackend.emergency.application.internal.outboundservices.acl.ExternalParkingMonitoringService;
import com.spotfinderbackend.emergency.domain.model.events.EmergencyResolvedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
public class EmergencyResolvedEventHandler {

    private final ExternalParkingMonitoringService externalParkingMonitoringService;

    public EmergencyResolvedEventHandler(ExternalParkingMonitoringService externalParkingMonitoringService) {
        this.externalParkingMonitoringService = externalParkingMonitoringService;
    }

    @EventListener
    public void on(EmergencyResolvedEvent event) {
        externalParkingMonitoringService.restoreLedsToNormalMode();
    }
}
