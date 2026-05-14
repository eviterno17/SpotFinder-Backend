package com.spotfinderbackend.parkingmonitoring.domain.model.valueobjects;

import com.spotfinderbackend.shared.domain.model.exceptions.BadRequestException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public record Address(@Column(name = "address", length = 200) String value) {

    public Address() { this(""); }

    public Address {
        if (value != null && value.length() > 200)
            throw new BadRequestException("Address must be at most 200 characters");
        if (value == null) value = "";
    }
}
