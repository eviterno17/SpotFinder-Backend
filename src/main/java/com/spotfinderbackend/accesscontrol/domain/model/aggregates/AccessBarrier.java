package com.spotfinderbackend.accesscontrol.domain.model.aggregates;

import com.spotfinderbackend.accesscontrol.domain.model.commands.RegisterBarrierCommand;
import com.spotfinderbackend.accesscontrol.domain.model.events.BarrierOpenedEvent;
import com.spotfinderbackend.accesscontrol.domain.model.valueobjects.BarrierCode;
import com.spotfinderbackend.accesscontrol.domain.model.valueobjects.BarrierPosition;
import com.spotfinderbackend.accesscontrol.domain.model.valueobjects.BarrierStatus;
import com.spotfinderbackend.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class AccessBarrier extends AuditableAbstractAggregateRoot<AccessBarrier> {

    @Embedded
    private BarrierCode barrierCode;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private BarrierPosition position;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private BarrierStatus status;

    @Column
    private Long facilityId;

    protected AccessBarrier() {}

    public AccessBarrier(RegisterBarrierCommand command) {
        this.barrierCode = new BarrierCode(command.barrierCode());
        this.position = command.position();
        this.status = BarrierStatus.CLOSED;
        this.facilityId = command.facilityId();
    }

    public void open(String reason) {
        this.status = BarrierStatus.OPEN;
        registerEvent(new BarrierOpenedEvent(this, barrierCode.code(), position,
                reason == null ? "MANUAL" : reason));
    }

    public void close() {
        this.status = BarrierStatus.CLOSED;
    }

    public void forceOpen(String reason) {
        this.status = BarrierStatus.OPEN;
        registerEvent(new BarrierOpenedEvent(this, barrierCode.code(), position,
                reason == null ? "EMERGENCY" : reason));
    }
}
