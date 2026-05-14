package com.spotfinderbackend.emergency.domain.model.aggregates;

import com.spotfinderbackend.emergency.domain.model.commands.TriggerEmergencyAlertCommand;
import com.spotfinderbackend.emergency.domain.model.events.EmergencyAlertTriggeredEvent;
import com.spotfinderbackend.emergency.domain.model.events.EmergencyResolvedEvent;
import com.spotfinderbackend.emergency.domain.model.valueobjects.EmergencySensorId;
import com.spotfinderbackend.emergency.domain.model.valueobjects.EmergencyStatus;
import com.spotfinderbackend.emergency.domain.model.valueobjects.EmergencyType;
import com.spotfinderbackend.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import com.spotfinderbackend.shared.domain.model.exceptions.BusinessRuleException;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Getter
public class EmergencyAlert extends AuditableAbstractAggregateRoot<EmergencyAlert> {

    private static final int DEFAULT_GAS_THRESHOLD_PPM = 900;

    @Embedded
    private EmergencySensorId sensorId;

    @Column(nullable = false)
    private int gasLevel;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private EmergencyType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private EmergencyStatus status;

    @Column(nullable = false)
    private LocalDateTime triggeredAt;

    @Column
    private LocalDateTime resolvedAt;

    @Column
    private Long resolvedBy;

    @Column(length = 200)
    private String sensorLocation;

    protected EmergencyAlert() {}

    public EmergencyAlert(TriggerEmergencyAlertCommand command) {
        this.sensorId = new EmergencySensorId(command.sensorId());
        this.gasLevel = command.gasLevel();
        this.type = command.type();
        this.status = EmergencyStatus.ACTIVE;
        this.triggeredAt = LocalDateTime.now();
        this.sensorLocation = command.sensorLocation();
        registerEvent(new EmergencyAlertTriggeredEvent(this, getId(), type, gasLevel, sensorLocation, triggeredAt));
    }

    public void resolve(Long adminUserId) {
        if (this.status == EmergencyStatus.RESOLVED)
            throw new BusinessRuleException("Emergency already resolved");
        this.status = EmergencyStatus.RESOLVED;
        this.resolvedAt = LocalDateTime.now();
        this.resolvedBy = adminUserId;
        registerEvent(new EmergencyResolvedEvent(this, getId(), resolvedAt, adminUserId));
    }

    public boolean isActive() { return this.status == EmergencyStatus.ACTIVE; }
    public boolean isResolved() { return this.status == EmergencyStatus.RESOLVED; }

    public boolean exceedsThreshold() {
        return this.gasLevel >= DEFAULT_GAS_THRESHOLD_PPM;
    }
}
