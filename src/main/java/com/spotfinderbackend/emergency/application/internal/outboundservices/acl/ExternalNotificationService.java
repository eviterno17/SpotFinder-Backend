package com.spotfinderbackend.emergency.application.internal.outboundservices.acl;

import com.spotfinderbackend.notifications.interfaces.acl.NotificationContextFacade;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("emergencyExternalNotificationService")
public class ExternalNotificationService {

    private final NotificationContextFacade facade;

    public ExternalNotificationService(NotificationContextFacade facade) {
        this.facade = facade;
    }

    public void broadcastEmergencyAlert(List<Long> userIds, String message, String sensorLocation) {
        if (userIds == null || userIds.isEmpty()) return;
        facade.broadcastNotification(userIds, "EMERGENCY_ALERT",
                "Alerta de emergencia",
                message,
                Map.of("sensorLocation", sensorLocation == null ? "" : sensorLocation));
    }

    public void sendAdminEmergencyAlert(Long adminUserId, String message, String sensorLocation, int gasLevel) {
        if (adminUserId == null) return;
        facade.sendNotification(adminUserId, "EMERGENCY_ALERT",
                "Alerta crítica",
                message,
                Map.of("sensorLocation", sensorLocation == null ? "" : sensorLocation,
                        "gasLevel", String.valueOf(gasLevel)));
    }
}
