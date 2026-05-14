package com.spotfinderbackend.accesscontrol.domain.model.valueobjects;

import com.spotfinderbackend.shared.domain.model.exceptions.BadRequestException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.util.regex.Pattern;

@Embeddable
public record LicensePlate(
        @Column(name = "plate_text", length = 12, nullable = false) String plateText,
        @Column(name = "plate_confidence") Double confidence
) {

    /** Peruvian standard plate format: 3 letters or alphanumerics + 3 numbers (e.g. A0B-123, ABC-123). */
    private static final Pattern PERUVIAN_PLATE = Pattern.compile("^[A-Z0-9]{3}-?[0-9]{3,4}$");

    public LicensePlate() { this("UNKNOWN", 0.0); }

    public LicensePlate {
        if (plateText == null || plateText.isBlank())
            throw new BadRequestException("Plate text is required");
        plateText = plateText.toUpperCase().trim();
        if (!"UNKNOWN".equals(plateText) && !PERUVIAN_PLATE.matcher(plateText).matches())
            throw new BadRequestException("Invalid plate format: " + plateText);
        if (confidence == null) confidence = 0.0;
        if (confidence < 0.0 || confidence > 1.0)
            throw new BadRequestException("Confidence must be in [0.0, 1.0]");
    }

    public LicensePlate(String plateText) { this(plateText, 0.0); }

    public boolean isHighConfidence() {
        return confidence != null && confidence > 0.85;
    }
}
