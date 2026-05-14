package com.spotfinderbackend.parkingmonitoring.domain.model.valueobjects;

import com.spotfinderbackend.shared.domain.model.exceptions.BadRequestException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public record FacilityId(@Column(name = "facility_id") Long value) {

    public FacilityId() { this(0L); }

    public FacilityId {
        if (value == null || value <= 0)
            throw new BadRequestException("FacilityId must be > 0");
    }
}
