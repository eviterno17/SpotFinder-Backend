package com.spotfinderbackend.parkingmonitoring.domain.model.events;

import com.spotfinderbackend.parkingmonitoring.domain.model.valueobjects.SlotStatus;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.time.LocalDateTime;

@Getter
public class SlotStatusChangedEvent extends ApplicationEvent {

    private final Long slotId;
    private final String slotCode;
    private final SlotStatus previousStatus;
    private final SlotStatus newStatus;
    private final LocalDateTime occurredAt;

    public SlotStatusChangedEvent(Object source, Long slotId, String slotCode,
                                  SlotStatus previousStatus, SlotStatus newStatus) {
        super(source);
        this.slotId = slotId;
        this.slotCode = slotCode;
        this.previousStatus = previousStatus;
        this.newStatus = newStatus;
        this.occurredAt = LocalDateTime.now();
    }
}
