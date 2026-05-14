package com.spotfinderbackend.analytics.domain.model.valueobjects;

import java.time.LocalDateTime;

public record SlotStatusSnapshot(Long slotId, String status, LocalDateTime timestamp) { }
