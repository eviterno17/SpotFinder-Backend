package com.spotfinderbackend.iam.domain.model.valueobjects;

import com.spotfinderbackend.shared.domain.model.exceptions.BadRequestException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.util.regex.Pattern;

/**
 * Peruvian vehicle plate identifier.
 * Accepts formats like {@code ABC-123}, {@code A1B-234}, with or without hyphen.
 * The canonical form stored is uppercase WITH hyphen between letters and digits.
 */
@Embeddable
public record Plate(@Column(name = "plate_text", length = 12, nullable = false, unique = true) String value) {

    private static final Pattern PERUVIAN_PLATE = Pattern.compile("^[A-Z0-9]{3}-?[0-9]{3,4}$");

    public Plate() { this("UNKNOWN"); }

    public Plate {
        if (value == null || value.isBlank())
            throw new BadRequestException("Plate is required");
        value = value.toUpperCase().trim().replace(" ", "");
        if (!"UNKNOWN".equals(value) && !PERUVIAN_PLATE.matcher(value).matches())
            throw new BadRequestException("Invalid plate format: " + value);
    }
}
