package com.spotfinderbackend.payments.domain.model.events;

import com.spotfinderbackend.payments.domain.model.valueobjects.PaymentMethod;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
public class PaymentSucceededEvent extends ApplicationEvent {
    private final Long paymentId;
    private final Long sessionId;
    private final BigDecimal amount;
    private final PaymentMethod paymentMethod;
    private final String transactionId;
    private final LocalDateTime paidAt;
    private final Long userId;

    public PaymentSucceededEvent(Object source, Long paymentId, Long sessionId, BigDecimal amount,
                                 PaymentMethod paymentMethod, String transactionId,
                                 LocalDateTime paidAt, Long userId) {
        super(source);
        this.paymentId = paymentId;
        this.sessionId = sessionId;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.transactionId = transactionId;
        this.paidAt = paidAt;
        this.userId = userId;
    }
}
