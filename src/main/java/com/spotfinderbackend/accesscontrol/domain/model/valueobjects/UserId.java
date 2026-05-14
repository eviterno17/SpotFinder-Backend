package com.spotfinderbackend.accesscontrol.domain.model.valueobjects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public record UserId(@Column(name = "user_id") Long value) {
    public UserId() { this(null); }
}
