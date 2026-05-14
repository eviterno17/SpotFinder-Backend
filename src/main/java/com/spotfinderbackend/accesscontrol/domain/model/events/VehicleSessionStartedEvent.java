package com.spotfinderbackend.accesscontrol.domain.model.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.time.LocalDateTime;

@Getter
public class VehicleSessionStartedEvent extends ApplicationEvent {
    private final Long sessionId;
    private final String licensePlate;
    private final LocalDateTime entryTimestamp;
    private final Long userId;

    public VehicleSessionStartedEvent(Object source, Long sessionId, String licensePlate,
                                      LocalDateTime entryTimestamp, Long userId) {
        super(source);
        this.sessionId = sessionId;
        this.licensePlate = licensePlate;
        this.entryTimestamp = entryTimestamp;
        this.userId = userId;
    }
}
