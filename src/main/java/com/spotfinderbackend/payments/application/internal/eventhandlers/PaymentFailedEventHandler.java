package com.spotfinderbackend.payments.application.internal.eventhandlers;

import com.spotfinderbackend.payments.application.internal.outboundservices.acl.ExternalNotificationService;
import com.spotfinderbackend.payments.domain.model.events.PaymentFailedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
public class PaymentFailedEventHandler {

    private final ExternalNotificationService externalNotificationService;

    public PaymentFailedEventHandler(ExternalNotificationService externalNotificationService) {
        this.externalNotificationService = externalNotificationService;
    }

    @EventListener
    public void on(PaymentFailedEvent event) {
        externalNotificationService.sendPaymentFailedNotification(event.getUserId(), event.getErrorDetail());
    }
}
