package com.spotfinderbackend.accesscontrol.domain.model.aggregates;

import com.spotfinderbackend.accesscontrol.domain.model.commands.CreateVehicleSessionCommand;
import com.spotfinderbackend.accesscontrol.domain.model.events.VehicleSessionEndedEvent;
import com.spotfinderbackend.accesscontrol.domain.model.events.VehicleSessionStartedEvent;
import com.spotfinderbackend.accesscontrol.domain.model.valueobjects.*;
import com.spotfinderbackend.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import com.spotfinderbackend.shared.domain.model.exceptions.BusinessRuleException;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.Duration;
import java.time.LocalDateTime;

@Entity
@Getter
public class VehicleSession extends AuditableAbstractAggregateRoot<VehicleSession> {

    @Embedded
    private LicensePlate licensePlate;

    @Column(nullable = false)
    private LocalDateTime entryTimestamp;

    @Column
    private LocalDateTime exitTimestamp;

    @Embedded
    private SlotId slotId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PaymentStatus paymentStatus;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private SessionStatus sessionStatus;

    @Embedded
    private UserId userId;

    protected VehicleSession() {}

    public VehicleSession(CreateVehicleSessionCommand command) {
        this.licensePlate = new LicensePlate(command.licensePlate());
        this.entryTimestamp = command.entryTimestamp();
        this.exitTimestamp = null;
        this.paymentStatus = PaymentStatus.PENDING;
        this.sessionStatus = SessionStatus.ACTIVE;
        this.userId = new UserId(command.userId());
        registerEvent(new VehicleSessionStartedEvent(this, getId(), licensePlate.plateText(),
                entryTimestamp, command.userId()));
    }

    public void end(LocalDateTime exitTimestamp) {
        if (this.sessionStatus == SessionStatus.COMPLETED)
            throw new BusinessRuleException("Session already completed");
        if (this.paymentStatus != PaymentStatus.PAID)
            throw new BusinessRuleException("Payment required before exit");
        this.exitTimestamp = exitTimestamp == null ? LocalDateTime.now() : exitTimestamp;
        this.sessionStatus = SessionStatus.COMPLETED;
        registerEvent(new VehicleSessionEndedEvent(this, getId(), licensePlate.plateText(),
                this.exitTimestamp, slotId == null ? null : slotId.value(),
                userId == null ? null : userId.value()));
    }

    public void markAsPaid() {
        if (this.sessionStatus != SessionStatus.ACTIVE)
            throw new BusinessRuleException("Only active sessions can be marked as paid");
        if (this.paymentStatus == PaymentStatus.PAID)
            throw new BusinessRuleException("Session already paid");
        this.paymentStatus = PaymentStatus.PAID;
    }

    public void assignSlot(Long slotId) {
        if (slotId == null) return;
        this.slotId = new SlotId(slotId);
    }

    public Duration calculateDuration() {
        var end = this.exitTimestamp == null ? LocalDateTime.now() : this.exitTimestamp;
        return Duration.between(this.entryTimestamp, end);
    }

    public boolean isActive() { return this.sessionStatus == SessionStatus.ACTIVE; }
    public boolean isPaid() { return this.paymentStatus == PaymentStatus.PAID; }
}
