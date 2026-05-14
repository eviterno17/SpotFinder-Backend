package com.spotfinderbackend.parkingmonitoring.domain.model.aggregates;

import com.spotfinderbackend.parkingmonitoring.domain.model.commands.RegisterParkingSlotCommand;
import com.spotfinderbackend.parkingmonitoring.domain.model.events.SlotStatusChangedEvent;
import com.spotfinderbackend.parkingmonitoring.domain.model.valueobjects.FacilityId;
import com.spotfinderbackend.parkingmonitoring.domain.model.valueobjects.SensorId;
import com.spotfinderbackend.parkingmonitoring.domain.model.valueobjects.SlotCode;
import com.spotfinderbackend.parkingmonitoring.domain.model.valueobjects.SlotStatus;
import com.spotfinderbackend.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import com.spotfinderbackend.shared.domain.model.exceptions.BusinessRuleException;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Getter
public class ParkingSlot extends AuditableAbstractAggregateRoot<ParkingSlot> {

    @Embedded
    private SlotCode slotCode;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private SlotStatus status;

    @Embedded
    private SensorId sensorId;

    @Embedded
    private FacilityId facilityId;

    private LocalDateTime lastUpdated;

    protected ParkingSlot() {}

    public ParkingSlot(RegisterParkingSlotCommand command) {
        this.slotCode = new SlotCode(command.slotCode());
        this.status = SlotStatus.AVAILABLE;
        this.sensorId = command.sensorId() == null ? new SensorId(null) : new SensorId(command.sensorId());
        this.facilityId = new FacilityId(command.facilityId());
        this.lastUpdated = LocalDateTime.now();
    }

    public void occupy() {
        if (this.status == SlotStatus.OCCUPIED)
            throw new BusinessRuleException("Slot is already occupied");
        if (this.status == SlotStatus.OUT_OF_SERVICE)
            throw new BusinessRuleException("Slot is out of service");
        applyTransition(SlotStatus.OCCUPIED);
    }

    public void release() {
        if (this.status == SlotStatus.AVAILABLE)
            throw new BusinessRuleException("Slot is already available");
        applyTransition(SlotStatus.AVAILABLE);
    }

    public void markOutOfService() {
        applyTransition(SlotStatus.OUT_OF_SERVICE);
    }

    public void markActive() {
        applyTransition(SlotStatus.AVAILABLE);
    }

    public void setEvacuationMode() {
        applyTransition(SlotStatus.EVACUATION);
    }

    public void restoreFromEvacuation(SlotStatus restoredStatus) {
        if (restoredStatus == null) restoredStatus = SlotStatus.AVAILABLE;
        applyTransition(restoredStatus);
    }

    /** Apply a status change as part of a sensor reading update. */
    public void changeStatusFromSensor(SlotStatus newStatus) {
        if (newStatus == null || newStatus == this.status) return;
        applyTransition(newStatus);
    }

    public boolean isAvailable() {
        return this.status == SlotStatus.AVAILABLE;
    }

    public boolean isOccupied() {
        return this.status == SlotStatus.OCCUPIED;
    }

    private void applyTransition(SlotStatus newStatus) {
        SlotStatus previous = this.status;
        this.status = newStatus;
        this.lastUpdated = LocalDateTime.now();
        registerEvent(new SlotStatusChangedEvent(this, getId(), slotCode.code(), previous, newStatus));
    }
}
