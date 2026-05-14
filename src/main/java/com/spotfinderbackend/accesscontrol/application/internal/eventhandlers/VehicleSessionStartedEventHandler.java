package com.spotfinderbackend.accesscontrol.application.internal.eventhandlers;

import com.spotfinderbackend.accesscontrol.application.internal.outboundservices.acl.ExternalNotificationService;
import com.spotfinderbackend.accesscontrol.domain.model.events.VehicleSessionStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
public class VehicleSessionStartedEventHandler {

    private final ExternalNotificationService externalNotificationService;

    public VehicleSessionStartedEventHandler(ExternalNotificationService externalNotificationService) {
        this.externalNotificationService = externalNotificationService;
    }

    @EventListener
    public void on(VehicleSessionStartedEvent event) {
        if (event.getUserId() == null) return;
        externalNotificationService.sendEntryNotification(event.getUserId(),
                event.getLicensePlate(), event.getEntryTimestamp());
    }
}
