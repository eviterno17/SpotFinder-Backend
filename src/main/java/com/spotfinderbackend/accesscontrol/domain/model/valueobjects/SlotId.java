package com.spotfinderbackend.accesscontrol.domain.model.valueobjects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public record SlotId(@Column(name = "slot_id") Long value) {
    public SlotId() { this(null); }
}
