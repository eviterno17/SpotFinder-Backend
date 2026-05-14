package com.spotfinderbackend.parkingmonitoring.domain.model.valueobjects;

import com.spotfinderbackend.shared.domain.model.exceptions.BadRequestException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public record SlotCode(@Column(name = "slot_code", length = 20, nullable = false, unique = true) String code) {

    public SlotCode() { this("UNDEFINED-0"); }

    public SlotCode {
        if (code == null || code.isBlank())
            throw new BadRequestException("Slot code is required");
        if (code.length() > 20)
            throw new BadRequestException("Slot code must be at most 20 characters");
    }
}
