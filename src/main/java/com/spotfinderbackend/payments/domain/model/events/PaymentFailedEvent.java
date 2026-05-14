package com.spotfinderbackend.payments.domain.model.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class PaymentFailedEvent extends ApplicationEvent {
    private final Long paymentId;
    private final Long sessionId;
    private final String errorDetail;
    private final Long userId;

    public PaymentFailedEvent(Object source, Long paymentId, Long sessionId, String errorDetail, Long userId) {
        super(source);
        this.paymentId = paymentId;
        this.sessionId = sessionId;
        this.errorDetail = errorDetail;
        this.userId = userId;
    }
}
