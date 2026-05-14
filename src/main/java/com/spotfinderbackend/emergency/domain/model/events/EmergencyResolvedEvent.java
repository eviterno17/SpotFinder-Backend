package com.spotfinderbackend.emergency.domain.model.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.time.LocalDateTime;

@Getter
public class EmergencyResolvedEvent extends ApplicationEvent {
    private final Long emergencyId;
    private final LocalDateTime resolvedAt;
    private final Long resolvedBy;

    public EmergencyResolvedEvent(Object source, Long emergencyId, LocalDateTime resolvedAt, Long resolvedBy) {
        super(source);
        this.emergencyId = emergencyId;
        this.resolvedAt = resolvedAt;
        this.resolvedBy = resolvedBy;
    }
}
