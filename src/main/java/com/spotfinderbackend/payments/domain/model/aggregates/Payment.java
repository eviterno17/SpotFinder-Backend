package com.spotfinderbackend.payments.domain.model.aggregates;

import com.spotfinderbackend.payments.domain.model.events.PaymentFailedEvent;
import com.spotfinderbackend.payments.domain.model.events.PaymentSucceededEvent;
import com.spotfinderbackend.payments.domain.model.valueobjects.*;
import com.spotfinderbackend.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import com.spotfinderbackend.shared.domain.model.exceptions.BusinessRuleException;
import com.spotfinderbackend.shared.domain.model.valueobjects.Money;
import jakarta.persistence.*;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
public class Payment extends AuditableAbstractAggregateRoot<Payment> {

    @Embedded
    private SessionId sessionId;

    @Embedded
    @AttributeOverride(name = "amount", column = @Column(name = "amount", precision = 15, scale = 2))
    @AttributeOverride(name = "currencyCode", column = @Column(name = "currency", length = 3))
    private Money amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PaymentTransactionStatus status;

    @Column(length = 100)
    private String transactionId;

    @Column(length = 500)
    private String receiptUrl;

    @Column
    private LocalDateTime paidAt;

    @Embedded
    private ParkingFee parkingFee;

    @Column
    private Long userId;

    protected Payment() {}

    public Payment(Long sessionId, Money amount, PaymentMethod method, ParkingFee fee, Long userId) {
        this.sessionId = new SessionId(sessionId);
        this.amount = amount;
        this.paymentMethod = method;
        this.status = PaymentTransactionStatus.PENDING;
        this.transactionId = null;
        this.receiptUrl = null;
        this.paidAt = null;
        this.parkingFee = fee == null ? new ParkingFee() : fee;
        this.userId = userId;
    }

    public void markAsCompleted(String transactionId, String receiptUrl) {
        if (this.status == PaymentTransactionStatus.COMPLETED)
            throw new BusinessRuleException("Payment already completed");
        this.status = PaymentTransactionStatus.COMPLETED;
        this.transactionId = transactionId;
        this.receiptUrl = receiptUrl;
        this.paidAt = LocalDateTime.now();
        registerEvent(new PaymentSucceededEvent(this, getId(), sessionId.value(),
                amount == null ? BigDecimal.ZERO : amount.amount(),
                paymentMethod, transactionId, paidAt, userId));
    }

    public void markAsFailed(String errorDetail) {
        this.status = PaymentTransactionStatus.FAILED;
        registerEvent(new PaymentFailedEvent(this, getId(), sessionId.value(), errorDetail, userId));
    }

    public boolean isCompleted() { return this.status == PaymentTransactionStatus.COMPLETED; }
    public boolean isFailed() { return this.status == PaymentTransactionStatus.FAILED; }
    public boolean isPending() { return this.status == PaymentTransactionStatus.PENDING; }
}
