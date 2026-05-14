package com.spotfinderbackend.payments.application.internal.eventhandlers;

import com.spotfinderbackend.payments.application.internal.outboundservices.acl.ExternalAccessControlService;
import com.spotfinderbackend.payments.application.internal.outboundservices.acl.ExternalNotificationService;
import com.spotfinderbackend.payments.domain.model.events.PaymentSucceededEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
public class PaymentSucceededEventHandler {

    private final ExternalAccessControlService externalAccessControlService;
    private final ExternalNotificationService externalNotificationService;

    public PaymentSucceededEventHandler(ExternalAccessControlService externalAccessControlService,
                                        ExternalNotificationService externalNotificationService) {
        this.externalAccessControlService = externalAccessControlService;
        this.externalNotificationService = externalNotificationService;
    }

    @EventListener
    public void on(PaymentSucceededEvent event) {
        externalAccessControlService.markSessionAsPaid(event.getSessionId());
        externalNotificationService.sendPaymentSuccessNotification(event.getUserId(),
                event.getAmount(),
                null); // receiptUrl is on the Payment aggregate; can be fetched if needed
    }
}
