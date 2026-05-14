package com.spotfinderbackend.accesscontrol.domain.model.events;

import com.spotfinderbackend.accesscontrol.domain.model.valueobjects.BarrierPosition;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class BarrierOpenedEvent extends ApplicationEvent {
    private final String barrierCode;
    private final BarrierPosition position;
    private final String reason;

    public BarrierOpenedEvent(Object source, String barrierCode, BarrierPosition position, String reason) {
        super(source);
        this.barrierCode = barrierCode;
        this.position = position;
        this.reason = reason;
    }
}
