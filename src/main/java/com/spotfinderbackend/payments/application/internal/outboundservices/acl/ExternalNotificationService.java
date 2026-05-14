package com.spotfinderbackend.payments.application.internal.outboundservices.acl;

import com.spotfinderbackend.notifications.interfaces.acl.NotificationContextFacade;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;

@Service("paymentsExternalNotificationService")
public class ExternalNotificationService {

    private final NotificationContextFacade facade;

    public ExternalNotificationService(NotificationContextFacade facade) {
        this.facade = facade;
    }

    public void sendPaymentSuccessNotification(Long userId, BigDecimal amount, String receiptUrl) {
        if (userId == null) return;
        facade.sendNotification(userId, "PAYMENT_SUCCESS",
                "Pago confirmado",
                "Tu pago por S/ " + amount + " fue procesado exitosamente",
                Map.of("amount", String.valueOf(amount),
                        "receiptUrl", receiptUrl == null ? "" : receiptUrl));
    }

    public void sendPaymentFailedNotification(Long userId, String errorDetail) {
        if (userId == null) return;
        facade.sendNotification(userId, "PAYMENT_FAILED",
                "Pago fallido",
                "Tu pago no pudo ser procesado: " + errorDetail,
                Map.of("error", errorDetail == null ? "" : errorDetail));
    }
}
