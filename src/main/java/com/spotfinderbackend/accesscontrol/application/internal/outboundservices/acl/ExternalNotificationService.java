package com.spotfinderbackend.accesscontrol.application.internal.outboundservices.acl;

import com.spotfinderbackend.notifications.interfaces.acl.NotificationContextFacade;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

@Service("accessControlExternalNotificationService")
public class ExternalNotificationService {

    private final NotificationContextFacade facade;

    public ExternalNotificationService(NotificationContextFacade facade) {
        this.facade = facade;
    }

    public void sendEntryNotification(Long userId, String licensePlate, LocalDateTime entryTime) {
        if (userId == null) return;
        facade.sendNotification(userId, "ENTRY_CONFIRMED",
                "Ingreso confirmado",
                "Tu vehículo " + licensePlate + " ingresó al estacionamiento a las " + entryTime,
                Map.of("licensePlate", licensePlate, "entryTime", entryTime.toString()));
    }

    public void sendPaymentReminderNotification(Long userId, Long sessionId) {
        if (userId == null) return;
        facade.sendNotification(userId, "PAYMENT_REMINDER",
                "Pago pendiente",
                "Debes completar el pago antes de salir del estacionamiento",
                Map.of("sessionId", String.valueOf(sessionId)));
    }

    public void sendSessionEndNotification(Long userId, String licensePlate) {
        if (userId == null) return;
        facade.sendNotification(userId, "SESSION_END",
                "Salida confirmada",
                "Tu vehículo " + licensePlate + " salió del estacionamiento",
                Map.of("licensePlate", licensePlate));
    }
}
