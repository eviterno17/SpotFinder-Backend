package com.spotfinderbackend.accesscontrol.domain.model.valueobjects;

import com.spotfinderbackend.shared.domain.model.exceptions.BadRequestException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public record BarrierCode(@Column(name = "barrier_code", length = 20, nullable = false, unique = true) String code) {

    public BarrierCode() { this("UNDEFINED-0"); }

    public BarrierCode {
        if (code == null || code.isBlank())
            throw new BadRequestException("Barrier code is required");
    }
}
