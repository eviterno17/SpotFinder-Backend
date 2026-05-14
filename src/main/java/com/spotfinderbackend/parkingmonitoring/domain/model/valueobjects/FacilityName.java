package com.spotfinderbackend.parkingmonitoring.domain.model.valueobjects;

import com.spotfinderbackend.shared.domain.model.exceptions.BadRequestException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public record FacilityName(@Column(name = "facility_name", length = 100) String value) {

    public FacilityName() { this("Unnamed"); }

    public FacilityName {
        if (value == null || value.isBlank())
            throw new BadRequestException("Facility name is required");
        if (value.length() > 100)
            throw new BadRequestException("Facility name must be at most 100 characters");
    }
}
