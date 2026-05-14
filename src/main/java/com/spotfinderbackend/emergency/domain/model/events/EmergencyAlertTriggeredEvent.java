package com.spotfinderbackend.emergency.domain.model.events;

import com.spotfinderbackend.emergency.domain.model.valueobjects.EmergencyType;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.time.LocalDateTime;

@Getter
public class EmergencyAlertTriggeredEvent extends ApplicationEvent {
    private final Long emergencyId;
    private final EmergencyType type;
    private final int gasLevel;
    private final String sensorLocation;
    private final LocalDateTime triggeredAt;

    public EmergencyAlertTriggeredEvent(Object source, Long emergencyId, EmergencyType type,
                                        int gasLevel, String sensorLocation, LocalDateTime triggeredAt) {
        super(source);
        this.emergencyId = emergencyId;
        this.type = type;
        this.gasLevel = gasLevel;
        this.sensorLocation = sensorLocation;
        this.triggeredAt = triggeredAt;
    }
}
