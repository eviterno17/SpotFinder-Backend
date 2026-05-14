package com.spotfinderbackend.emergency.application.internal.eventhandlers;

import com.spotfinderbackend.emergency.application.internal.outboundservices.acl.ExternalAccessControlService;
import com.spotfinderbackend.emergency.application.internal.outboundservices.acl.ExternalNotificationService;
import com.spotfinderbackend.emergency.application.internal.outboundservices.acl.ExternalParkingMonitoringService;
import com.spotfinderbackend.emergency.domain.model.events.EmergencyAlertTriggeredEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

/**
 * Runs the evacuation protocol in parallel across the system:
 * <ol>
 *   <li>Open ALL barriers (Access Control)</li>
 *   <li>Switch LEDs to evacuation mode (Parking Monitoring)</li>
 *   <li>Broadcast push notification to drivers with active sessions</li>
 * </ol>
 */
@Service
public class EmergencyAlertTriggeredEventHandler {

    private final ExternalAccessControlService externalAccessControlService;
    private final ExternalParkingMonitoringService externalParkingMonitoringService;
    private final ExternalNotificationService externalNotificationService;

    public EmergencyAlertTriggeredEventHandler(ExternalAccessControlService externalAccessControlService,
                                               ExternalParkingMonitoringService externalParkingMonitoringService,
                                               ExternalNotificationService externalNotificationService) {
        this.externalAccessControlService = externalAccessControlService;
        this.externalParkingMonitoringService = externalParkingMonitoringService;
        this.externalNotificationService = externalNotificationService;
    }

    @EventListener
    public void on(EmergencyAlertTriggeredEvent event) {
        externalAccessControlService.openAllBarriers("EMERGENCY");
        externalParkingMonitoringService.setAllLedsToEvacuationMode();

        var driverUserIds = externalAccessControlService.getActiveSessionUserIds();
        var message = "Emergencia: " + event.getType().name() + " detectada en " + event.getSensorLocation()
                + ". Evacúe inmediatamente.";
        externalNotificationService.broadcastEmergencyAlert(driverUserIds, message, event.getSensorLocation());
    }
}
