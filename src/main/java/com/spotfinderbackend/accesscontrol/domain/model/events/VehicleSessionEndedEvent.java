package com.spotfinderbackend.accesscontrol.domain.model.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.time.LocalDateTime;

@Getter
public class VehicleSessionEndedEvent extends ApplicationEvent {
    private final Long sessionId;
    private final String licensePlate;
    private final LocalDateTime exitTimestamp;
    private final Long slotId;
    private final Long userId;

    public VehicleSessionEndedEvent(Object source, Long sessionId, String licensePlate,
                                    LocalDateTime exitTimestamp, Long slotId, Long userId) {
        super(source);
        this.sessionId = sessionId;
        this.licensePlate = licensePlate;
        this.exitTimestamp = exitTimestamp;
        this.slotId = slotId;
        this.userId = userId;
    }
}
